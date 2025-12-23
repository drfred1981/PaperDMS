import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IPerformanceMetric, NewPerformanceMetric } from '../performance-metric.model';

export type PartialUpdatePerformanceMetric = Partial<IPerformanceMetric> & Pick<IPerformanceMetric, 'id'>;

type RestOf<T extends IPerformanceMetric | NewPerformanceMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestPerformanceMetric = RestOf<IPerformanceMetric>;

export type NewRestPerformanceMetric = RestOf<NewPerformanceMetric>;

export type PartialUpdateRestPerformanceMetric = RestOf<PartialUpdatePerformanceMetric>;

export type EntityResponseType = HttpResponse<IPerformanceMetric>;
export type EntityArrayResponseType = HttpResponse<IPerformanceMetric[]>;

@Injectable({ providedIn: 'root' })
export class PerformanceMetricService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/performance-metrics', 'reportingservice');

  create(performanceMetric: NewPerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceMetric);
    return this.http
      .post<RestPerformanceMetric>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(performanceMetric: IPerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceMetric);
    return this.http
      .put<RestPerformanceMetric>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPerformanceMetricIdentifier(performanceMetric))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(performanceMetric: PartialUpdatePerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceMetric);
    return this.http
      .patch<RestPerformanceMetric>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPerformanceMetricIdentifier(performanceMetric))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPerformanceMetric>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPerformanceMetric[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getPerformanceMetricIdentifier(performanceMetric: Pick<IPerformanceMetric, 'id'>): number {
    return performanceMetric.id;
  }

  comparePerformanceMetric(o1: Pick<IPerformanceMetric, 'id'> | null, o2: Pick<IPerformanceMetric, 'id'> | null): boolean {
    return o1 && o2 ? this.getPerformanceMetricIdentifier(o1) === this.getPerformanceMetricIdentifier(o2) : o1 === o2;
  }

  addPerformanceMetricToCollectionIfMissing<Type extends Pick<IPerformanceMetric, 'id'>>(
    performanceMetricCollection: Type[],
    ...performanceMetricsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const performanceMetrics: Type[] = performanceMetricsToCheck.filter(isPresent);
    if (performanceMetrics.length > 0) {
      const performanceMetricCollectionIdentifiers = performanceMetricCollection.map(performanceMetricItem =>
        this.getPerformanceMetricIdentifier(performanceMetricItem),
      );
      const performanceMetricsToAdd = performanceMetrics.filter(performanceMetricItem => {
        const performanceMetricIdentifier = this.getPerformanceMetricIdentifier(performanceMetricItem);
        if (performanceMetricCollectionIdentifiers.includes(performanceMetricIdentifier)) {
          return false;
        }
        performanceMetricCollectionIdentifiers.push(performanceMetricIdentifier);
        return true;
      });
      return [...performanceMetricsToAdd, ...performanceMetricCollection];
    }
    return performanceMetricCollection;
  }

  protected convertDateFromClient<T extends IPerformanceMetric | NewPerformanceMetric | PartialUpdatePerformanceMetric>(
    performanceMetric: T,
  ): RestOf<T> {
    return {
      ...performanceMetric,
      timestamp: performanceMetric.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPerformanceMetric: RestPerformanceMetric): IPerformanceMetric {
    return {
      ...restPerformanceMetric,
      timestamp: restPerformanceMetric.timestamp ? dayjs(restPerformanceMetric.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPerformanceMetric>): HttpResponse<IPerformanceMetric> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPerformanceMetric[]>): HttpResponse<IPerformanceMetric[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
