import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWebhookSubscription, NewWebhookSubscription } from '../webhook-subscription.model';

export type PartialUpdateWebhookSubscription = Partial<IWebhookSubscription> & Pick<IWebhookSubscription, 'id'>;

type RestOf<T extends IWebhookSubscription | NewWebhookSubscription> = Omit<
  T,
  'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
> & {
  lastTriggerDate?: string | null;
  lastSuccessDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestWebhookSubscription = RestOf<IWebhookSubscription>;

export type NewRestWebhookSubscription = RestOf<NewWebhookSubscription>;

export type PartialUpdateRestWebhookSubscription = RestOf<PartialUpdateWebhookSubscription>;

export type EntityResponseType = HttpResponse<IWebhookSubscription>;
export type EntityArrayResponseType = HttpResponse<IWebhookSubscription[]>;

@Injectable({ providedIn: 'root' })
export class WebhookSubscriptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/webhook-subscriptions', 'notificationservice');

  create(webhookSubscription: NewWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookSubscription);
    return this.http
      .post<RestWebhookSubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(webhookSubscription: IWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookSubscription);
    return this.http
      .put<RestWebhookSubscription>(`${this.resourceUrl}/${this.getWebhookSubscriptionIdentifier(webhookSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(webhookSubscription: PartialUpdateWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookSubscription);
    return this.http
      .patch<RestWebhookSubscription>(`${this.resourceUrl}/${this.getWebhookSubscriptionIdentifier(webhookSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWebhookSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWebhookSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWebhookSubscriptionIdentifier(webhookSubscription: Pick<IWebhookSubscription, 'id'>): number {
    return webhookSubscription.id;
  }

  compareWebhookSubscription(o1: Pick<IWebhookSubscription, 'id'> | null, o2: Pick<IWebhookSubscription, 'id'> | null): boolean {
    return o1 && o2 ? this.getWebhookSubscriptionIdentifier(o1) === this.getWebhookSubscriptionIdentifier(o2) : o1 === o2;
  }

  addWebhookSubscriptionToCollectionIfMissing<Type extends Pick<IWebhookSubscription, 'id'>>(
    webhookSubscriptionCollection: Type[],
    ...webhookSubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const webhookSubscriptions: Type[] = webhookSubscriptionsToCheck.filter(isPresent);
    if (webhookSubscriptions.length > 0) {
      const webhookSubscriptionCollectionIdentifiers = webhookSubscriptionCollection.map(webhookSubscriptionItem =>
        this.getWebhookSubscriptionIdentifier(webhookSubscriptionItem),
      );
      const webhookSubscriptionsToAdd = webhookSubscriptions.filter(webhookSubscriptionItem => {
        const webhookSubscriptionIdentifier = this.getWebhookSubscriptionIdentifier(webhookSubscriptionItem);
        if (webhookSubscriptionCollectionIdentifiers.includes(webhookSubscriptionIdentifier)) {
          return false;
        }
        webhookSubscriptionCollectionIdentifiers.push(webhookSubscriptionIdentifier);
        return true;
      });
      return [...webhookSubscriptionsToAdd, ...webhookSubscriptionCollection];
    }
    return webhookSubscriptionCollection;
  }

  protected convertDateFromClient<T extends IWebhookSubscription | NewWebhookSubscription | PartialUpdateWebhookSubscription>(
    webhookSubscription: T,
  ): RestOf<T> {
    return {
      ...webhookSubscription,
      lastTriggerDate: webhookSubscription.lastTriggerDate?.toJSON() ?? null,
      lastSuccessDate: webhookSubscription.lastSuccessDate?.toJSON() ?? null,
      createdDate: webhookSubscription.createdDate?.toJSON() ?? null,
      lastModifiedDate: webhookSubscription.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWebhookSubscription: RestWebhookSubscription): IWebhookSubscription {
    return {
      ...restWebhookSubscription,
      lastTriggerDate: restWebhookSubscription.lastTriggerDate ? dayjs(restWebhookSubscription.lastTriggerDate) : undefined,
      lastSuccessDate: restWebhookSubscription.lastSuccessDate ? dayjs(restWebhookSubscription.lastSuccessDate) : undefined,
      createdDate: restWebhookSubscription.createdDate ? dayjs(restWebhookSubscription.createdDate) : undefined,
      lastModifiedDate: restWebhookSubscription.lastModifiedDate ? dayjs(restWebhookSubscription.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWebhookSubscription>): HttpResponse<IWebhookSubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWebhookSubscription[]>): HttpResponse<IWebhookSubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
