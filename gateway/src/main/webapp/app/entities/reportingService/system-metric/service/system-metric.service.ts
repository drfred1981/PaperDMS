import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISystemMetric, NewSystemMetric } from '../system-metric.model';

export type PartialUpdateSystemMetric = Partial<ISystemMetric> & Pick<ISystemMetric, 'id'>;

type RestOf<T extends ISystemMetric | NewSystemMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestSystemMetric = RestOf<ISystemMetric>;

export type NewRestSystemMetric = RestOf<NewSystemMetric>;

export type PartialUpdateRestSystemMetric = RestOf<PartialUpdateSystemMetric>;

export type EntityResponseType = HttpResponse<ISystemMetric>;
export type EntityArrayResponseType = HttpResponse<ISystemMetric[]>;

@Injectable({ providedIn: 'root' })
export class SystemMetricService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-metrics', 'reportingservice');

  create(systemMetric: NewSystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemMetric);
    return this.http
      .post<RestSystemMetric>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(systemMetric: ISystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemMetric);
    return this.http
      .put<RestSystemMetric>(`${this.resourceUrl}/${encodeURIComponent(this.getSystemMetricIdentifier(systemMetric))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(systemMetric: PartialUpdateSystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemMetric);
    return this.http
      .patch<RestSystemMetric>(`${this.resourceUrl}/${encodeURIComponent(this.getSystemMetricIdentifier(systemMetric))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSystemMetric>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSystemMetric[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSystemMetricIdentifier(systemMetric: Pick<ISystemMetric, 'id'>): number {
    return systemMetric.id;
  }

  compareSystemMetric(o1: Pick<ISystemMetric, 'id'> | null, o2: Pick<ISystemMetric, 'id'> | null): boolean {
    return o1 && o2 ? this.getSystemMetricIdentifier(o1) === this.getSystemMetricIdentifier(o2) : o1 === o2;
  }

  addSystemMetricToCollectionIfMissing<Type extends Pick<ISystemMetric, 'id'>>(
    systemMetricCollection: Type[],
    ...systemMetricsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const systemMetrics: Type[] = systemMetricsToCheck.filter(isPresent);
    if (systemMetrics.length > 0) {
      const systemMetricCollectionIdentifiers = systemMetricCollection.map(systemMetricItem =>
        this.getSystemMetricIdentifier(systemMetricItem),
      );
      const systemMetricsToAdd = systemMetrics.filter(systemMetricItem => {
        const systemMetricIdentifier = this.getSystemMetricIdentifier(systemMetricItem);
        if (systemMetricCollectionIdentifiers.includes(systemMetricIdentifier)) {
          return false;
        }
        systemMetricCollectionIdentifiers.push(systemMetricIdentifier);
        return true;
      });
      return [...systemMetricsToAdd, ...systemMetricCollection];
    }
    return systemMetricCollection;
  }

  protected convertDateFromClient<T extends ISystemMetric | NewSystemMetric | PartialUpdateSystemMetric>(systemMetric: T): RestOf<T> {
    return {
      ...systemMetric,
      timestamp: systemMetric.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSystemMetric: RestSystemMetric): ISystemMetric {
    return {
      ...restSystemMetric,
      timestamp: restSystemMetric.timestamp ? dayjs(restSystemMetric.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSystemMetric>): HttpResponse<ISystemMetric> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSystemMetric[]>): HttpResponse<ISystemMetric[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
