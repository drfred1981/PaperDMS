import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringAlert, NewMonitoringAlert } from '../monitoring-alert.model';

export type PartialUpdateMonitoringAlert = Partial<IMonitoringAlert> & Pick<IMonitoringAlert, 'id'>;

type RestOf<T extends IMonitoringAlert | NewMonitoringAlert> = Omit<T, 'triggeredDate' | 'acknowledgedDate' | 'resolvedDate'> & {
  triggeredDate?: string | null;
  acknowledgedDate?: string | null;
  resolvedDate?: string | null;
};

export type RestMonitoringAlert = RestOf<IMonitoringAlert>;

export type NewRestMonitoringAlert = RestOf<NewMonitoringAlert>;

export type PartialUpdateRestMonitoringAlert = RestOf<PartialUpdateMonitoringAlert>;

export type EntityResponseType = HttpResponse<IMonitoringAlert>;
export type EntityArrayResponseType = HttpResponse<IMonitoringAlert[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringAlertService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-alerts', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/monitoring-alerts/_search', 'monitoringservice');

  create(monitoringAlert: NewMonitoringAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlert);
    return this.http
      .post<RestMonitoringAlert>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringAlert: IMonitoringAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlert);
    return this.http
      .put<RestMonitoringAlert>(`${this.resourceUrl}/${this.getMonitoringAlertIdentifier(monitoringAlert)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringAlert: PartialUpdateMonitoringAlert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlert);
    return this.http
      .patch<RestMonitoringAlert>(`${this.resourceUrl}/${this.getMonitoringAlertIdentifier(monitoringAlert)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringAlert>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringAlert[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringAlert[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringAlert[]>()], asapScheduler)),
    );
  }

  getMonitoringAlertIdentifier(monitoringAlert: Pick<IMonitoringAlert, 'id'>): number {
    return monitoringAlert.id;
  }

  compareMonitoringAlert(o1: Pick<IMonitoringAlert, 'id'> | null, o2: Pick<IMonitoringAlert, 'id'> | null): boolean {
    return o1 && o2 ? this.getMonitoringAlertIdentifier(o1) === this.getMonitoringAlertIdentifier(o2) : o1 === o2;
  }

  addMonitoringAlertToCollectionIfMissing<Type extends Pick<IMonitoringAlert, 'id'>>(
    monitoringAlertCollection: Type[],
    ...monitoringAlertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringAlerts: Type[] = monitoringAlertsToCheck.filter(isPresent);
    if (monitoringAlerts.length > 0) {
      const monitoringAlertCollectionIdentifiers = monitoringAlertCollection.map(monitoringAlertItem =>
        this.getMonitoringAlertIdentifier(monitoringAlertItem),
      );
      const monitoringAlertsToAdd = monitoringAlerts.filter(monitoringAlertItem => {
        const monitoringAlertIdentifier = this.getMonitoringAlertIdentifier(monitoringAlertItem);
        if (monitoringAlertCollectionIdentifiers.includes(monitoringAlertIdentifier)) {
          return false;
        }
        monitoringAlertCollectionIdentifiers.push(monitoringAlertIdentifier);
        return true;
      });
      return [...monitoringAlertsToAdd, ...monitoringAlertCollection];
    }
    return monitoringAlertCollection;
  }

  protected convertDateFromClient<T extends IMonitoringAlert | NewMonitoringAlert | PartialUpdateMonitoringAlert>(
    monitoringAlert: T,
  ): RestOf<T> {
    return {
      ...monitoringAlert,
      triggeredDate: monitoringAlert.triggeredDate?.toJSON() ?? null,
      acknowledgedDate: monitoringAlert.acknowledgedDate?.toJSON() ?? null,
      resolvedDate: monitoringAlert.resolvedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringAlert: RestMonitoringAlert): IMonitoringAlert {
    return {
      ...restMonitoringAlert,
      triggeredDate: restMonitoringAlert.triggeredDate ? dayjs(restMonitoringAlert.triggeredDate) : undefined,
      acknowledgedDate: restMonitoringAlert.acknowledgedDate ? dayjs(restMonitoringAlert.acknowledgedDate) : undefined,
      resolvedDate: restMonitoringAlert.resolvedDate ? dayjs(restMonitoringAlert.resolvedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringAlert>): HttpResponse<IMonitoringAlert> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringAlert[]>): HttpResponse<IMonitoringAlert[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
