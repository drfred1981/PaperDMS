import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringServiceStatus, NewMonitoringServiceStatus } from '../monitoring-service-status.model';

export type PartialUpdateMonitoringServiceStatus = Partial<IMonitoringServiceStatus> & Pick<IMonitoringServiceStatus, 'id'>;

type RestOf<T extends IMonitoringServiceStatus | NewMonitoringServiceStatus> = Omit<T, 'lastPing'> & {
  lastPing?: string | null;
};

export type RestMonitoringServiceStatus = RestOf<IMonitoringServiceStatus>;

export type NewRestMonitoringServiceStatus = RestOf<NewMonitoringServiceStatus>;

export type PartialUpdateRestMonitoringServiceStatus = RestOf<PartialUpdateMonitoringServiceStatus>;

export type EntityResponseType = HttpResponse<IMonitoringServiceStatus>;
export type EntityArrayResponseType = HttpResponse<IMonitoringServiceStatus[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringServiceStatusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-service-statuses', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/monitoring-service-statuses/_search',
    'monitoringservice',
  );

  create(monitoringServiceStatus: NewMonitoringServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringServiceStatus);
    return this.http
      .post<RestMonitoringServiceStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringServiceStatus: IMonitoringServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringServiceStatus);
    return this.http
      .put<RestMonitoringServiceStatus>(`${this.resourceUrl}/${this.getMonitoringServiceStatusIdentifier(monitoringServiceStatus)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringServiceStatus: PartialUpdateMonitoringServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringServiceStatus);
    return this.http
      .patch<RestMonitoringServiceStatus>(
        `${this.resourceUrl}/${this.getMonitoringServiceStatusIdentifier(monitoringServiceStatus)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringServiceStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringServiceStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringServiceStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringServiceStatus[]>()], asapScheduler)),
    );
  }

  getMonitoringServiceStatusIdentifier(monitoringServiceStatus: Pick<IMonitoringServiceStatus, 'id'>): number {
    return monitoringServiceStatus.id;
  }

  compareMonitoringServiceStatus(
    o1: Pick<IMonitoringServiceStatus, 'id'> | null,
    o2: Pick<IMonitoringServiceStatus, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getMonitoringServiceStatusIdentifier(o1) === this.getMonitoringServiceStatusIdentifier(o2) : o1 === o2;
  }

  addMonitoringServiceStatusToCollectionIfMissing<Type extends Pick<IMonitoringServiceStatus, 'id'>>(
    monitoringServiceStatusCollection: Type[],
    ...monitoringServiceStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringServiceStatuses: Type[] = monitoringServiceStatusesToCheck.filter(isPresent);
    if (monitoringServiceStatuses.length > 0) {
      const monitoringServiceStatusCollectionIdentifiers = monitoringServiceStatusCollection.map(monitoringServiceStatusItem =>
        this.getMonitoringServiceStatusIdentifier(monitoringServiceStatusItem),
      );
      const monitoringServiceStatusesToAdd = monitoringServiceStatuses.filter(monitoringServiceStatusItem => {
        const monitoringServiceStatusIdentifier = this.getMonitoringServiceStatusIdentifier(monitoringServiceStatusItem);
        if (monitoringServiceStatusCollectionIdentifiers.includes(monitoringServiceStatusIdentifier)) {
          return false;
        }
        monitoringServiceStatusCollectionIdentifiers.push(monitoringServiceStatusIdentifier);
        return true;
      });
      return [...monitoringServiceStatusesToAdd, ...monitoringServiceStatusCollection];
    }
    return monitoringServiceStatusCollection;
  }

  protected convertDateFromClient<T extends IMonitoringServiceStatus | NewMonitoringServiceStatus | PartialUpdateMonitoringServiceStatus>(
    monitoringServiceStatus: T,
  ): RestOf<T> {
    return {
      ...monitoringServiceStatus,
      lastPing: monitoringServiceStatus.lastPing?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringServiceStatus: RestMonitoringServiceStatus): IMonitoringServiceStatus {
    return {
      ...restMonitoringServiceStatus,
      lastPing: restMonitoringServiceStatus.lastPing ? dayjs(restMonitoringServiceStatus.lastPing) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringServiceStatus>): HttpResponse<IMonitoringServiceStatus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringServiceStatus[]>): HttpResponse<IMonitoringServiceStatus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
