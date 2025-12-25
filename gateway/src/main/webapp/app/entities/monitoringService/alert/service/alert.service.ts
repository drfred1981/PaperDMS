import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlert, NewAlert } from '../alert.model';

export type PartialUpdateAlert = Partial<IAlert> & Pick<IAlert, 'id'>;

type RestOf<T extends IAlert | NewAlert> = Omit<T, 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'> & {
  triggeredDate?: string | null;
  acknowledgedDate?: string | null;
  resolvedDate?: string | null;
};

export type RestAlert = RestOf<IAlert>;

export type NewRestAlert = RestOf<NewAlert>;

export type PartialUpdateRestAlert = RestOf<PartialUpdateAlert>;

export type EntityResponseType = HttpResponse<IAlert>;
export type EntityArrayResponseType = HttpResponse<IAlert[]>;

@Injectable({ providedIn: 'root' })
export class AlertService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alerts', 'monitoringservice');

  create(alert: NewAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alert);
    return this.http.post<RestAlert>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alert: IAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alert);
    return this.http
      .put<RestAlert>(`${this.resourceUrl}/${this.getAlertIdentifier(alert)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alert: PartialUpdateAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alert);
    return this.http
      .patch<RestAlert>(`${this.resourceUrl}/${this.getAlertIdentifier(alert)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlert>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlert[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlertIdentifier(alert: Pick<IAlert, 'id'>): number {
    return alert.id;
  }

  compareAlert(o1: Pick<IAlert, 'id'> | null, o2: Pick<IAlert, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlertIdentifier(o1) === this.getAlertIdentifier(o2) : o1 === o2;
  }

  addAlertToCollectionIfMissing<Type extends Pick<IAlert, 'id'>>(
    alertCollection: Type[],
    ...alertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alerts: Type[] = alertsToCheck.filter(isPresent);
    if (alerts.length > 0) {
      const alertCollectionIdentifiers = alertCollection.map(alertItem => this.getAlertIdentifier(alertItem));
      const alertsToAdd = alerts.filter(alertItem => {
        const alertIdentifier = this.getAlertIdentifier(alertItem);
        if (alertCollectionIdentifiers.includes(alertIdentifier)) {
          return false;
        }
        alertCollectionIdentifiers.push(alertIdentifier);
        return true;
      });
      return [...alertsToAdd, ...alertCollection];
    }
    return alertCollection;
  }

  protected convertDateFromClient<T extends IAlert | NewAlert | PartialUpdateAlert>(alert: T): RestOf<T> {
    return {
      ...alert,
      triggeredDate: alert.triggeredDate?.toJSON() ?? null,
      acknowledgedDate: alert.acknowledgedDate?.toJSON() ?? null,
      resolvedDate: alert.resolvedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlert: RestAlert): IAlert {
    return {
      ...restAlert,
      triggeredDate: restAlert.triggeredDate ? dayjs(restAlert.triggeredDate) : undefined,
      acknowledgedDate: restAlert.acknowledgedDate ? dayjs(restAlert.acknowledgedDate) : undefined,
      resolvedDate: restAlert.resolvedDate ? dayjs(restAlert.resolvedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlert>): HttpResponse<IAlert> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlert[]>): HttpResponse<IAlert[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
