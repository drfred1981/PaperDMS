import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringSystemHealth, NewMonitoringSystemHealth } from '../monitoring-system-health.model';

export type PartialUpdateMonitoringSystemHealth = Partial<IMonitoringSystemHealth> & Pick<IMonitoringSystemHealth, 'id'>;

type RestOf<T extends IMonitoringSystemHealth | NewMonitoringSystemHealth> = Omit<T, 'lastCheck'> & {
  lastCheck?: string | null;
};

export type RestMonitoringSystemHealth = RestOf<IMonitoringSystemHealth>;

export type NewRestMonitoringSystemHealth = RestOf<NewMonitoringSystemHealth>;

export type PartialUpdateRestMonitoringSystemHealth = RestOf<PartialUpdateMonitoringSystemHealth>;

export type EntityResponseType = HttpResponse<IMonitoringSystemHealth>;
export type EntityArrayResponseType = HttpResponse<IMonitoringSystemHealth[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringSystemHealthService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-system-healths', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/monitoring-system-healths/_search', 'monitoringservice');

  create(monitoringSystemHealth: NewMonitoringSystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringSystemHealth);
    return this.http
      .post<RestMonitoringSystemHealth>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringSystemHealth: IMonitoringSystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringSystemHealth);
    return this.http
      .put<RestMonitoringSystemHealth>(`${this.resourceUrl}/${this.getMonitoringSystemHealthIdentifier(monitoringSystemHealth)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringSystemHealth: PartialUpdateMonitoringSystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringSystemHealth);
    return this.http
      .patch<RestMonitoringSystemHealth>(`${this.resourceUrl}/${this.getMonitoringSystemHealthIdentifier(monitoringSystemHealth)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringSystemHealth>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringSystemHealth[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringSystemHealth[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringSystemHealth[]>()], asapScheduler)),
    );
  }

  getMonitoringSystemHealthIdentifier(monitoringSystemHealth: Pick<IMonitoringSystemHealth, 'id'>): number {
    return monitoringSystemHealth.id;
  }

  compareMonitoringSystemHealth(o1: Pick<IMonitoringSystemHealth, 'id'> | null, o2: Pick<IMonitoringSystemHealth, 'id'> | null): boolean {
    return o1 && o2 ? this.getMonitoringSystemHealthIdentifier(o1) === this.getMonitoringSystemHealthIdentifier(o2) : o1 === o2;
  }

  addMonitoringSystemHealthToCollectionIfMissing<Type extends Pick<IMonitoringSystemHealth, 'id'>>(
    monitoringSystemHealthCollection: Type[],
    ...monitoringSystemHealthsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringSystemHealths: Type[] = monitoringSystemHealthsToCheck.filter(isPresent);
    if (monitoringSystemHealths.length > 0) {
      const monitoringSystemHealthCollectionIdentifiers = monitoringSystemHealthCollection.map(monitoringSystemHealthItem =>
        this.getMonitoringSystemHealthIdentifier(monitoringSystemHealthItem),
      );
      const monitoringSystemHealthsToAdd = monitoringSystemHealths.filter(monitoringSystemHealthItem => {
        const monitoringSystemHealthIdentifier = this.getMonitoringSystemHealthIdentifier(monitoringSystemHealthItem);
        if (monitoringSystemHealthCollectionIdentifiers.includes(monitoringSystemHealthIdentifier)) {
          return false;
        }
        monitoringSystemHealthCollectionIdentifiers.push(monitoringSystemHealthIdentifier);
        return true;
      });
      return [...monitoringSystemHealthsToAdd, ...monitoringSystemHealthCollection];
    }
    return monitoringSystemHealthCollection;
  }

  protected convertDateFromClient<T extends IMonitoringSystemHealth | NewMonitoringSystemHealth | PartialUpdateMonitoringSystemHealth>(
    monitoringSystemHealth: T,
  ): RestOf<T> {
    return {
      ...monitoringSystemHealth,
      lastCheck: monitoringSystemHealth.lastCheck?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringSystemHealth: RestMonitoringSystemHealth): IMonitoringSystemHealth {
    return {
      ...restMonitoringSystemHealth,
      lastCheck: restMonitoringSystemHealth.lastCheck ? dayjs(restMonitoringSystemHealth.lastCheck) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringSystemHealth>): HttpResponse<IMonitoringSystemHealth> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringSystemHealth[]>): HttpResponse<IMonitoringSystemHealth[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
