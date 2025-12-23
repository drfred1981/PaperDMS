import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { INotificationEvent, NewNotificationEvent } from '../notification-event.model';

export type PartialUpdateNotificationEvent = Partial<INotificationEvent> & Pick<INotificationEvent, 'id'>;

type RestOf<T extends INotificationEvent | NewNotificationEvent> = Omit<T, 'eventDate' | 'processedDate'> & {
  eventDate?: string | null;
  processedDate?: string | null;
};

export type RestNotificationEvent = RestOf<INotificationEvent>;

export type NewRestNotificationEvent = RestOf<NewNotificationEvent>;

export type PartialUpdateRestNotificationEvent = RestOf<PartialUpdateNotificationEvent>;

export type EntityResponseType = HttpResponse<INotificationEvent>;
export type EntityArrayResponseType = HttpResponse<INotificationEvent[]>;

@Injectable({ providedIn: 'root' })
export class NotificationEventService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-events', 'notificationservice');

  create(notificationEvent: NewNotificationEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationEvent);
    return this.http
      .post<RestNotificationEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationEvent: INotificationEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationEvent);
    return this.http
      .put<RestNotificationEvent>(
        `${this.resourceUrl}/${encodeURIComponent(this.getNotificationEventIdentifier(notificationEvent))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationEvent: PartialUpdateNotificationEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationEvent);
    return this.http
      .patch<RestNotificationEvent>(
        `${this.resourceUrl}/${encodeURIComponent(this.getNotificationEventIdentifier(notificationEvent))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationEvent>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getNotificationEventIdentifier(notificationEvent: Pick<INotificationEvent, 'id'>): number {
    return notificationEvent.id;
  }

  compareNotificationEvent(o1: Pick<INotificationEvent, 'id'> | null, o2: Pick<INotificationEvent, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationEventIdentifier(o1) === this.getNotificationEventIdentifier(o2) : o1 === o2;
  }

  addNotificationEventToCollectionIfMissing<Type extends Pick<INotificationEvent, 'id'>>(
    notificationEventCollection: Type[],
    ...notificationEventsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationEvents: Type[] = notificationEventsToCheck.filter(isPresent);
    if (notificationEvents.length > 0) {
      const notificationEventCollectionIdentifiers = notificationEventCollection.map(notificationEventItem =>
        this.getNotificationEventIdentifier(notificationEventItem),
      );
      const notificationEventsToAdd = notificationEvents.filter(notificationEventItem => {
        const notificationEventIdentifier = this.getNotificationEventIdentifier(notificationEventItem);
        if (notificationEventCollectionIdentifiers.includes(notificationEventIdentifier)) {
          return false;
        }
        notificationEventCollectionIdentifiers.push(notificationEventIdentifier);
        return true;
      });
      return [...notificationEventsToAdd, ...notificationEventCollection];
    }
    return notificationEventCollection;
  }

  protected convertDateFromClient<T extends INotificationEvent | NewNotificationEvent | PartialUpdateNotificationEvent>(
    notificationEvent: T,
  ): RestOf<T> {
    return {
      ...notificationEvent,
      eventDate: notificationEvent.eventDate?.toJSON() ?? null,
      processedDate: notificationEvent.processedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationEvent: RestNotificationEvent): INotificationEvent {
    return {
      ...restNotificationEvent,
      eventDate: restNotificationEvent.eventDate ? dayjs(restNotificationEvent.eventDate) : undefined,
      processedDate: restNotificationEvent.processedDate ? dayjs(restNotificationEvent.processedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationEvent>): HttpResponse<INotificationEvent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationEvent[]>): HttpResponse<INotificationEvent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
