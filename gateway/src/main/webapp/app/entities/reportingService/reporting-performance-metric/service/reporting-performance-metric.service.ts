import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingPerformanceMetric, NewReportingPerformanceMetric } from '../reporting-performance-metric.model';

export type PartialUpdateReportingPerformanceMetric = Partial<IReportingPerformanceMetric> & Pick<IReportingPerformanceMetric, 'id'>;

type RestOf<T extends IReportingPerformanceMetric | NewReportingPerformanceMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestReportingPerformanceMetric = RestOf<IReportingPerformanceMetric>;

export type NewRestReportingPerformanceMetric = RestOf<NewReportingPerformanceMetric>;

export type PartialUpdateRestReportingPerformanceMetric = RestOf<PartialUpdateReportingPerformanceMetric>;

export type EntityResponseType = HttpResponse<IReportingPerformanceMetric>;
export type EntityArrayResponseType = HttpResponse<IReportingPerformanceMetric[]>;

@Injectable({ providedIn: 'root' })
export class ReportingPerformanceMetricService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-performance-metrics', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/reporting-performance-metrics/_search',
    'reportingservice',
  );

  create(reportingPerformanceMetric: NewReportingPerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingPerformanceMetric);
    return this.http
      .post<RestReportingPerformanceMetric>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportingPerformanceMetric: IReportingPerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingPerformanceMetric);
    return this.http
      .put<RestReportingPerformanceMetric>(
        `${this.resourceUrl}/${this.getReportingPerformanceMetricIdentifier(reportingPerformanceMetric)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportingPerformanceMetric: PartialUpdateReportingPerformanceMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingPerformanceMetric);
    return this.http
      .patch<RestReportingPerformanceMetric>(
        `${this.resourceUrl}/${this.getReportingPerformanceMetricIdentifier(reportingPerformanceMetric)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportingPerformanceMetric>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportingPerformanceMetric[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportingPerformanceMetric[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportingPerformanceMetric[]>()], asapScheduler)),
    );
  }

  getReportingPerformanceMetricIdentifier(reportingPerformanceMetric: Pick<IReportingPerformanceMetric, 'id'>): number {
    return reportingPerformanceMetric.id;
  }

  compareReportingPerformanceMetric(
    o1: Pick<IReportingPerformanceMetric, 'id'> | null,
    o2: Pick<IReportingPerformanceMetric, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getReportingPerformanceMetricIdentifier(o1) === this.getReportingPerformanceMetricIdentifier(o2) : o1 === o2;
  }

  addReportingPerformanceMetricToCollectionIfMissing<Type extends Pick<IReportingPerformanceMetric, 'id'>>(
    reportingPerformanceMetricCollection: Type[],
    ...reportingPerformanceMetricsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingPerformanceMetrics: Type[] = reportingPerformanceMetricsToCheck.filter(isPresent);
    if (reportingPerformanceMetrics.length > 0) {
      const reportingPerformanceMetricCollectionIdentifiers = reportingPerformanceMetricCollection.map(reportingPerformanceMetricItem =>
        this.getReportingPerformanceMetricIdentifier(reportingPerformanceMetricItem),
      );
      const reportingPerformanceMetricsToAdd = reportingPerformanceMetrics.filter(reportingPerformanceMetricItem => {
        const reportingPerformanceMetricIdentifier = this.getReportingPerformanceMetricIdentifier(reportingPerformanceMetricItem);
        if (reportingPerformanceMetricCollectionIdentifiers.includes(reportingPerformanceMetricIdentifier)) {
          return false;
        }
        reportingPerformanceMetricCollectionIdentifiers.push(reportingPerformanceMetricIdentifier);
        return true;
      });
      return [...reportingPerformanceMetricsToAdd, ...reportingPerformanceMetricCollection];
    }
    return reportingPerformanceMetricCollection;
  }

  protected convertDateFromClient<
    T extends IReportingPerformanceMetric | NewReportingPerformanceMetric | PartialUpdateReportingPerformanceMetric,
  >(reportingPerformanceMetric: T): RestOf<T> {
    return {
      ...reportingPerformanceMetric,
      timestamp: reportingPerformanceMetric.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportingPerformanceMetric: RestReportingPerformanceMetric): IReportingPerformanceMetric {
    return {
      ...restReportingPerformanceMetric,
      timestamp: restReportingPerformanceMetric.timestamp ? dayjs(restReportingPerformanceMetric.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportingPerformanceMetric>): HttpResponse<IReportingPerformanceMetric> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestReportingPerformanceMetric[]>,
  ): HttpResponse<IReportingPerformanceMetric[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
