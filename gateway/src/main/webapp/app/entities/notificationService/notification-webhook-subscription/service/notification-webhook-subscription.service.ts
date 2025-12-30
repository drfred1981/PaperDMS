import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INotificationWebhookSubscription, NewNotificationWebhookSubscription } from '../notification-webhook-subscription.model';

export type PartialUpdateNotificationWebhookSubscription = Partial<INotificationWebhookSubscription> &
  Pick<INotificationWebhookSubscription, 'id'>;

type RestOf<T extends INotificationWebhookSubscription | NewNotificationWebhookSubscription> = Omit<
  T,
  'lastTriggerDate' | 'lastSuccessDate' | 'createdDate' | 'lastModifiedDate'
> & {
  lastTriggerDate?: string | null;
  lastSuccessDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestNotificationWebhookSubscription = RestOf<INotificationWebhookSubscription>;

export type NewRestNotificationWebhookSubscription = RestOf<NewNotificationWebhookSubscription>;

export type PartialUpdateRestNotificationWebhookSubscription = RestOf<PartialUpdateNotificationWebhookSubscription>;

export type EntityResponseType = HttpResponse<INotificationWebhookSubscription>;
export type EntityArrayResponseType = HttpResponse<INotificationWebhookSubscription[]>;

@Injectable({ providedIn: 'root' })
export class NotificationWebhookSubscriptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-webhook-subscriptions', 'notificationservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/notification-webhook-subscriptions/_search',
    'notificationservice',
  );

  create(notificationWebhookSubscription: NewNotificationWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookSubscription);
    return this.http
      .post<RestNotificationWebhookSubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationWebhookSubscription: INotificationWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookSubscription);
    return this.http
      .put<RestNotificationWebhookSubscription>(
        `${this.resourceUrl}/${this.getNotificationWebhookSubscriptionIdentifier(notificationWebhookSubscription)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationWebhookSubscription: PartialUpdateNotificationWebhookSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookSubscription);
    return this.http
      .patch<RestNotificationWebhookSubscription>(
        `${this.resourceUrl}/${this.getNotificationWebhookSubscriptionIdentifier(notificationWebhookSubscription)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationWebhookSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationWebhookSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestNotificationWebhookSubscription[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<INotificationWebhookSubscription[]>()], asapScheduler)),
    );
  }

  getNotificationWebhookSubscriptionIdentifier(notificationWebhookSubscription: Pick<INotificationWebhookSubscription, 'id'>): number {
    return notificationWebhookSubscription.id;
  }

  compareNotificationWebhookSubscription(
    o1: Pick<INotificationWebhookSubscription, 'id'> | null,
    o2: Pick<INotificationWebhookSubscription, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getNotificationWebhookSubscriptionIdentifier(o1) === this.getNotificationWebhookSubscriptionIdentifier(o2)
      : o1 === o2;
  }

  addNotificationWebhookSubscriptionToCollectionIfMissing<Type extends Pick<INotificationWebhookSubscription, 'id'>>(
    notificationWebhookSubscriptionCollection: Type[],
    ...notificationWebhookSubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationWebhookSubscriptions: Type[] = notificationWebhookSubscriptionsToCheck.filter(isPresent);
    if (notificationWebhookSubscriptions.length > 0) {
      const notificationWebhookSubscriptionCollectionIdentifiers = notificationWebhookSubscriptionCollection.map(
        notificationWebhookSubscriptionItem => this.getNotificationWebhookSubscriptionIdentifier(notificationWebhookSubscriptionItem),
      );
      const notificationWebhookSubscriptionsToAdd = notificationWebhookSubscriptions.filter(notificationWebhookSubscriptionItem => {
        const notificationWebhookSubscriptionIdentifier =
          this.getNotificationWebhookSubscriptionIdentifier(notificationWebhookSubscriptionItem);
        if (notificationWebhookSubscriptionCollectionIdentifiers.includes(notificationWebhookSubscriptionIdentifier)) {
          return false;
        }
        notificationWebhookSubscriptionCollectionIdentifiers.push(notificationWebhookSubscriptionIdentifier);
        return true;
      });
      return [...notificationWebhookSubscriptionsToAdd, ...notificationWebhookSubscriptionCollection];
    }
    return notificationWebhookSubscriptionCollection;
  }

  protected convertDateFromClient<
    T extends INotificationWebhookSubscription | NewNotificationWebhookSubscription | PartialUpdateNotificationWebhookSubscription,
  >(notificationWebhookSubscription: T): RestOf<T> {
    return {
      ...notificationWebhookSubscription,
      lastTriggerDate: notificationWebhookSubscription.lastTriggerDate?.toJSON() ?? null,
      lastSuccessDate: notificationWebhookSubscription.lastSuccessDate?.toJSON() ?? null,
      createdDate: notificationWebhookSubscription.createdDate?.toJSON() ?? null,
      lastModifiedDate: notificationWebhookSubscription.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(
    restNotificationWebhookSubscription: RestNotificationWebhookSubscription,
  ): INotificationWebhookSubscription {
    return {
      ...restNotificationWebhookSubscription,
      lastTriggerDate: restNotificationWebhookSubscription.lastTriggerDate
        ? dayjs(restNotificationWebhookSubscription.lastTriggerDate)
        : undefined,
      lastSuccessDate: restNotificationWebhookSubscription.lastSuccessDate
        ? dayjs(restNotificationWebhookSubscription.lastSuccessDate)
        : undefined,
      createdDate: restNotificationWebhookSubscription.createdDate ? dayjs(restNotificationWebhookSubscription.createdDate) : undefined,
      lastModifiedDate: restNotificationWebhookSubscription.lastModifiedDate
        ? dayjs(restNotificationWebhookSubscription.lastModifiedDate)
        : undefined,
    };
  }

  protected convertResponseFromServer(
    res: HttpResponse<RestNotificationWebhookSubscription>,
  ): HttpResponse<INotificationWebhookSubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestNotificationWebhookSubscription[]>,
  ): HttpResponse<INotificationWebhookSubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
