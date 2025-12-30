import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingSystemMetric, NewReportingSystemMetric } from '../reporting-system-metric.model';

export type PartialUpdateReportingSystemMetric = Partial<IReportingSystemMetric> & Pick<IReportingSystemMetric, 'id'>;

type RestOf<T extends IReportingSystemMetric | NewReportingSystemMetric> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestReportingSystemMetric = RestOf<IReportingSystemMetric>;

export type NewRestReportingSystemMetric = RestOf<NewReportingSystemMetric>;

export type PartialUpdateRestReportingSystemMetric = RestOf<PartialUpdateReportingSystemMetric>;

export type EntityResponseType = HttpResponse<IReportingSystemMetric>;
export type EntityArrayResponseType = HttpResponse<IReportingSystemMetric[]>;

@Injectable({ providedIn: 'root' })
export class ReportingSystemMetricService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-system-metrics', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/reporting-system-metrics/_search', 'reportingservice');

  create(reportingSystemMetric: NewReportingSystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingSystemMetric);
    return this.http
      .post<RestReportingSystemMetric>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportingSystemMetric: IReportingSystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingSystemMetric);
    return this.http
      .put<RestReportingSystemMetric>(`${this.resourceUrl}/${this.getReportingSystemMetricIdentifier(reportingSystemMetric)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportingSystemMetric: PartialUpdateReportingSystemMetric): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingSystemMetric);
    return this.http
      .patch<RestReportingSystemMetric>(`${this.resourceUrl}/${this.getReportingSystemMetricIdentifier(reportingSystemMetric)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportingSystemMetric>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportingSystemMetric[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportingSystemMetric[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportingSystemMetric[]>()], asapScheduler)),
    );
  }

  getReportingSystemMetricIdentifier(reportingSystemMetric: Pick<IReportingSystemMetric, 'id'>): number {
    return reportingSystemMetric.id;
  }

  compareReportingSystemMetric(o1: Pick<IReportingSystemMetric, 'id'> | null, o2: Pick<IReportingSystemMetric, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportingSystemMetricIdentifier(o1) === this.getReportingSystemMetricIdentifier(o2) : o1 === o2;
  }

  addReportingSystemMetricToCollectionIfMissing<Type extends Pick<IReportingSystemMetric, 'id'>>(
    reportingSystemMetricCollection: Type[],
    ...reportingSystemMetricsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingSystemMetrics: Type[] = reportingSystemMetricsToCheck.filter(isPresent);
    if (reportingSystemMetrics.length > 0) {
      const reportingSystemMetricCollectionIdentifiers = reportingSystemMetricCollection.map(reportingSystemMetricItem =>
        this.getReportingSystemMetricIdentifier(reportingSystemMetricItem),
      );
      const reportingSystemMetricsToAdd = reportingSystemMetrics.filter(reportingSystemMetricItem => {
        const reportingSystemMetricIdentifier = this.getReportingSystemMetricIdentifier(reportingSystemMetricItem);
        if (reportingSystemMetricCollectionIdentifiers.includes(reportingSystemMetricIdentifier)) {
          return false;
        }
        reportingSystemMetricCollectionIdentifiers.push(reportingSystemMetricIdentifier);
        return true;
      });
      return [...reportingSystemMetricsToAdd, ...reportingSystemMetricCollection];
    }
    return reportingSystemMetricCollection;
  }

  protected convertDateFromClient<T extends IReportingSystemMetric | NewReportingSystemMetric | PartialUpdateReportingSystemMetric>(
    reportingSystemMetric: T,
  ): RestOf<T> {
    return {
      ...reportingSystemMetric,
      timestamp: reportingSystemMetric.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportingSystemMetric: RestReportingSystemMetric): IReportingSystemMetric {
    return {
      ...restReportingSystemMetric,
      timestamp: restReportingSystemMetric.timestamp ? dayjs(restReportingSystemMetric.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportingSystemMetric>): HttpResponse<IReportingSystemMetric> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportingSystemMetric[]>): HttpResponse<IReportingSystemMetric[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
