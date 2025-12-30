import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingScheduledReport, NewReportingScheduledReport } from '../reporting-scheduled-report.model';

export type PartialUpdateReportingScheduledReport = Partial<IReportingScheduledReport> & Pick<IReportingScheduledReport, 'id'>;

type RestOf<T extends IReportingScheduledReport | NewReportingScheduledReport> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

export type RestReportingScheduledReport = RestOf<IReportingScheduledReport>;

export type NewRestReportingScheduledReport = RestOf<NewReportingScheduledReport>;

export type PartialUpdateRestReportingScheduledReport = RestOf<PartialUpdateReportingScheduledReport>;

export type EntityResponseType = HttpResponse<IReportingScheduledReport>;
export type EntityArrayResponseType = HttpResponse<IReportingScheduledReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportingScheduledReportService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-scheduled-reports', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/reporting-scheduled-reports/_search', 'reportingservice');

  create(reportingScheduledReport: NewReportingScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingScheduledReport);
    return this.http
      .post<RestReportingScheduledReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportingScheduledReport: IReportingScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingScheduledReport);
    return this.http
      .put<RestReportingScheduledReport>(
        `${this.resourceUrl}/${this.getReportingScheduledReportIdentifier(reportingScheduledReport)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportingScheduledReport: PartialUpdateReportingScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingScheduledReport);
    return this.http
      .patch<RestReportingScheduledReport>(
        `${this.resourceUrl}/${this.getReportingScheduledReportIdentifier(reportingScheduledReport)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportingScheduledReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportingScheduledReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportingScheduledReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportingScheduledReport[]>()], asapScheduler)),
    );
  }

  getReportingScheduledReportIdentifier(reportingScheduledReport: Pick<IReportingScheduledReport, 'id'>): number {
    return reportingScheduledReport.id;
  }

  compareReportingScheduledReport(
    o1: Pick<IReportingScheduledReport, 'id'> | null,
    o2: Pick<IReportingScheduledReport, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getReportingScheduledReportIdentifier(o1) === this.getReportingScheduledReportIdentifier(o2) : o1 === o2;
  }

  addReportingScheduledReportToCollectionIfMissing<Type extends Pick<IReportingScheduledReport, 'id'>>(
    reportingScheduledReportCollection: Type[],
    ...reportingScheduledReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingScheduledReports: Type[] = reportingScheduledReportsToCheck.filter(isPresent);
    if (reportingScheduledReports.length > 0) {
      const reportingScheduledReportCollectionIdentifiers = reportingScheduledReportCollection.map(reportingScheduledReportItem =>
        this.getReportingScheduledReportIdentifier(reportingScheduledReportItem),
      );
      const reportingScheduledReportsToAdd = reportingScheduledReports.filter(reportingScheduledReportItem => {
        const reportingScheduledReportIdentifier = this.getReportingScheduledReportIdentifier(reportingScheduledReportItem);
        if (reportingScheduledReportCollectionIdentifiers.includes(reportingScheduledReportIdentifier)) {
          return false;
        }
        reportingScheduledReportCollectionIdentifiers.push(reportingScheduledReportIdentifier);
        return true;
      });
      return [...reportingScheduledReportsToAdd, ...reportingScheduledReportCollection];
    }
    return reportingScheduledReportCollection;
  }

  protected convertDateFromClient<
    T extends IReportingScheduledReport | NewReportingScheduledReport | PartialUpdateReportingScheduledReport,
  >(reportingScheduledReport: T): RestOf<T> {
    return {
      ...reportingScheduledReport,
      lastRun: reportingScheduledReport.lastRun?.toJSON() ?? null,
      nextRun: reportingScheduledReport.nextRun?.toJSON() ?? null,
      createdDate: reportingScheduledReport.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportingScheduledReport: RestReportingScheduledReport): IReportingScheduledReport {
    return {
      ...restReportingScheduledReport,
      lastRun: restReportingScheduledReport.lastRun ? dayjs(restReportingScheduledReport.lastRun) : undefined,
      nextRun: restReportingScheduledReport.nextRun ? dayjs(restReportingScheduledReport.nextRun) : undefined,
      createdDate: restReportingScheduledReport.createdDate ? dayjs(restReportingScheduledReport.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportingScheduledReport>): HttpResponse<IReportingScheduledReport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportingScheduledReport[]>): HttpResponse<IReportingScheduledReport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
