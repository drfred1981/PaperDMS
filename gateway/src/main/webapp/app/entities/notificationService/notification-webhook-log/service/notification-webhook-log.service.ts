import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INotificationWebhookLog, NewNotificationWebhookLog } from '../notification-webhook-log.model';

export type PartialUpdateNotificationWebhookLog = Partial<INotificationWebhookLog> & Pick<INotificationWebhookLog, 'id'>;

type RestOf<T extends INotificationWebhookLog | NewNotificationWebhookLog> = Omit<T, 'sentDate'> & {
  sentDate?: string | null;
};

export type RestNotificationWebhookLog = RestOf<INotificationWebhookLog>;

export type NewRestNotificationWebhookLog = RestOf<NewNotificationWebhookLog>;

export type PartialUpdateRestNotificationWebhookLog = RestOf<PartialUpdateNotificationWebhookLog>;

export type EntityResponseType = HttpResponse<INotificationWebhookLog>;
export type EntityArrayResponseType = HttpResponse<INotificationWebhookLog[]>;

@Injectable({ providedIn: 'root' })
export class NotificationWebhookLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-webhook-logs', 'notificationservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/notification-webhook-logs/_search',
    'notificationservice',
  );

  create(notificationWebhookLog: NewNotificationWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookLog);
    return this.http
      .post<RestNotificationWebhookLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationWebhookLog: INotificationWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookLog);
    return this.http
      .put<RestNotificationWebhookLog>(`${this.resourceUrl}/${this.getNotificationWebhookLogIdentifier(notificationWebhookLog)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationWebhookLog: PartialUpdateNotificationWebhookLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationWebhookLog);
    return this.http
      .patch<RestNotificationWebhookLog>(`${this.resourceUrl}/${this.getNotificationWebhookLogIdentifier(notificationWebhookLog)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationWebhookLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationWebhookLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestNotificationWebhookLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<INotificationWebhookLog[]>()], asapScheduler)),
    );
  }

  getNotificationWebhookLogIdentifier(notificationWebhookLog: Pick<INotificationWebhookLog, 'id'>): number {
    return notificationWebhookLog.id;
  }

  compareNotificationWebhookLog(o1: Pick<INotificationWebhookLog, 'id'> | null, o2: Pick<INotificationWebhookLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationWebhookLogIdentifier(o1) === this.getNotificationWebhookLogIdentifier(o2) : o1 === o2;
  }

  addNotificationWebhookLogToCollectionIfMissing<Type extends Pick<INotificationWebhookLog, 'id'>>(
    notificationWebhookLogCollection: Type[],
    ...notificationWebhookLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationWebhookLogs: Type[] = notificationWebhookLogsToCheck.filter(isPresent);
    if (notificationWebhookLogs.length > 0) {
      const notificationWebhookLogCollectionIdentifiers = notificationWebhookLogCollection.map(notificationWebhookLogItem =>
        this.getNotificationWebhookLogIdentifier(notificationWebhookLogItem),
      );
      const notificationWebhookLogsToAdd = notificationWebhookLogs.filter(notificationWebhookLogItem => {
        const notificationWebhookLogIdentifier = this.getNotificationWebhookLogIdentifier(notificationWebhookLogItem);
        if (notificationWebhookLogCollectionIdentifiers.includes(notificationWebhookLogIdentifier)) {
          return false;
        }
        notificationWebhookLogCollectionIdentifiers.push(notificationWebhookLogIdentifier);
        return true;
      });
      return [...notificationWebhookLogsToAdd, ...notificationWebhookLogCollection];
    }
    return notificationWebhookLogCollection;
  }

  protected convertDateFromClient<T extends INotificationWebhookLog | NewNotificationWebhookLog | PartialUpdateNotificationWebhookLog>(
    notificationWebhookLog: T,
  ): RestOf<T> {
    return {
      ...notificationWebhookLog,
      sentDate: notificationWebhookLog.sentDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationWebhookLog: RestNotificationWebhookLog): INotificationWebhookLog {
    return {
      ...restNotificationWebhookLog,
      sentDate: restNotificationWebhookLog.sentDate ? dayjs(restNotificationWebhookLog.sentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationWebhookLog>): HttpResponse<INotificationWebhookLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationWebhookLog[]>): HttpResponse<INotificationWebhookLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
