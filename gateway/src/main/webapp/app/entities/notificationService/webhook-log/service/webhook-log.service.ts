import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWebhookLog, NewWebhookLog } from '../webhook-log.model';

export type PartialUpdateWebhookLog = Partial<IWebhookLog> & Pick<IWebhookLog, 'id'>;

type RestOf<T extends IWebhookLog | NewWebhookLog> = Omit<T, 'sentDate'> & {
  sentDate?: string | null;
};

export type RestWebhookLog = RestOf<IWebhookLog>;

export type NewRestWebhookLog = RestOf<NewWebhookLog>;

export type PartialUpdateRestWebhookLog = RestOf<PartialUpdateWebhookLog>;

export type EntityResponseType = HttpResponse<IWebhookLog>;
export type EntityArrayResponseType = HttpResponse<IWebhookLog[]>;

@Injectable({ providedIn: 'root' })
export class WebhookLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/webhook-logs', 'notificationservice');

  create(webhookLog: NewWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookLog);
    return this.http
      .post<RestWebhookLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(webhookLog: IWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookLog);
    return this.http
      .put<RestWebhookLog>(`${this.resourceUrl}/${this.getWebhookLogIdentifier(webhookLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(webhookLog: PartialUpdateWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webhookLog);
    return this.http
      .patch<RestWebhookLog>(`${this.resourceUrl}/${this.getWebhookLogIdentifier(webhookLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWebhookLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWebhookLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWebhookLogIdentifier(webhookLog: Pick<IWebhookLog, 'id'>): number {
    return webhookLog.id;
  }

  compareWebhookLog(o1: Pick<IWebhookLog, 'id'> | null, o2: Pick<IWebhookLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getWebhookLogIdentifier(o1) === this.getWebhookLogIdentifier(o2) : o1 === o2;
  }

  addWebhookLogToCollectionIfMissing<Type extends Pick<IWebhookLog, 'id'>>(
    webhookLogCollection: Type[],
    ...webhookLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const webhookLogs: Type[] = webhookLogsToCheck.filter(isPresent);
    if (webhookLogs.length > 0) {
      const webhookLogCollectionIdentifiers = webhookLogCollection.map(webhookLogItem => this.getWebhookLogIdentifier(webhookLogItem));
      const webhookLogsToAdd = webhookLogs.filter(webhookLogItem => {
        const webhookLogIdentifier = this.getWebhookLogIdentifier(webhookLogItem);
        if (webhookLogCollectionIdentifiers.includes(webhookLogIdentifier)) {
          return false;
        }
        webhookLogCollectionIdentifiers.push(webhookLogIdentifier);
        return true;
      });
      return [...webhookLogsToAdd, ...webhookLogCollection];
    }
    return webhookLogCollection;
  }

  protected convertDateFromClient<T extends IWebhookLog | NewWebhookLog | PartialUpdateWebhookLog>(webhookLog: T): RestOf<T> {
    return {
      ...webhookLog,
      sentDate: webhookLog.sentDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWebhookLog: RestWebhookLog): IWebhookLog {
    return {
      ...restWebhookLog,
      sentDate: restWebhookLog.sentDate ? dayjs(restWebhookLog.sentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWebhookLog>): HttpResponse<IWebhookLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWebhookLog[]>): HttpResponse<IWebhookLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
