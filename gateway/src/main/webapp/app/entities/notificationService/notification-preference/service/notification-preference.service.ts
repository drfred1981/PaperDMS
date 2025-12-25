import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotificationPreference, NewNotificationPreference } from '../notification-preference.model';

export type PartialUpdateNotificationPreference = Partial<INotificationPreference> & Pick<INotificationPreference, 'id'>;

type RestOf<T extends INotificationPreference | NewNotificationPreference> = Omit<T, 'lastModifiedDate'> & {
  lastModifiedDate?: string | null;
};

export type RestNotificationPreference = RestOf<INotificationPreference>;

export type NewRestNotificationPreference = RestOf<NewNotificationPreference>;

export type PartialUpdateRestNotificationPreference = RestOf<PartialUpdateNotificationPreference>;

export type EntityResponseType = HttpResponse<INotificationPreference>;
export type EntityArrayResponseType = HttpResponse<INotificationPreference[]>;

@Injectable({ providedIn: 'root' })
export class NotificationPreferenceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-preferences', 'notificationservice');

  create(notificationPreference: NewNotificationPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationPreference);
    return this.http
      .post<RestNotificationPreference>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationPreference: INotificationPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationPreference);
    return this.http
      .put<RestNotificationPreference>(`${this.resourceUrl}/${this.getNotificationPreferenceIdentifier(notificationPreference)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationPreference: PartialUpdateNotificationPreference): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationPreference);
    return this.http
      .patch<RestNotificationPreference>(`${this.resourceUrl}/${this.getNotificationPreferenceIdentifier(notificationPreference)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationPreference>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationPreference[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotificationPreferenceIdentifier(notificationPreference: Pick<INotificationPreference, 'id'>): number {
    return notificationPreference.id;
  }

  compareNotificationPreference(o1: Pick<INotificationPreference, 'id'> | null, o2: Pick<INotificationPreference, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationPreferenceIdentifier(o1) === this.getNotificationPreferenceIdentifier(o2) : o1 === o2;
  }

  addNotificationPreferenceToCollectionIfMissing<Type extends Pick<INotificationPreference, 'id'>>(
    notificationPreferenceCollection: Type[],
    ...notificationPreferencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationPreferences: Type[] = notificationPreferencesToCheck.filter(isPresent);
    if (notificationPreferences.length > 0) {
      const notificationPreferenceCollectionIdentifiers = notificationPreferenceCollection.map(notificationPreferenceItem =>
        this.getNotificationPreferenceIdentifier(notificationPreferenceItem),
      );
      const notificationPreferencesToAdd = notificationPreferences.filter(notificationPreferenceItem => {
        const notificationPreferenceIdentifier = this.getNotificationPreferenceIdentifier(notificationPreferenceItem);
        if (notificationPreferenceCollectionIdentifiers.includes(notificationPreferenceIdentifier)) {
          return false;
        }
        notificationPreferenceCollectionIdentifiers.push(notificationPreferenceIdentifier);
        return true;
      });
      return [...notificationPreferencesToAdd, ...notificationPreferenceCollection];
    }
    return notificationPreferenceCollection;
  }

  protected convertDateFromClient<T extends INotificationPreference | NewNotificationPreference | PartialUpdateNotificationPreference>(
    notificationPreference: T,
  ): RestOf<T> {
    return {
      ...notificationPreference,
      lastModifiedDate: notificationPreference.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationPreference: RestNotificationPreference): INotificationPreference {
    return {
      ...restNotificationPreference,
      lastModifiedDate: restNotificationPreference.lastModifiedDate ? dayjs(restNotificationPreference.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationPreference>): HttpResponse<INotificationPreference> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationPreference[]>): HttpResponse<INotificationPreference[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
