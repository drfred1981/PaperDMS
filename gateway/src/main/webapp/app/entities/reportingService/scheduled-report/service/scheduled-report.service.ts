import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IScheduledReport, NewScheduledReport } from '../scheduled-report.model';

export type PartialUpdateScheduledReport = Partial<IScheduledReport> & Pick<IScheduledReport, 'id'>;

type RestOf<T extends IScheduledReport | NewScheduledReport> = Omit<T, 'lastRun' | 'nextRun' | 'createdDate'> & {
  lastRun?: string | null;
  nextRun?: string | null;
  createdDate?: string | null;
};

export type RestScheduledReport = RestOf<IScheduledReport>;

export type NewRestScheduledReport = RestOf<NewScheduledReport>;

export type PartialUpdateRestScheduledReport = RestOf<PartialUpdateScheduledReport>;

export type EntityResponseType = HttpResponse<IScheduledReport>;
export type EntityArrayResponseType = HttpResponse<IScheduledReport[]>;

@Injectable({ providedIn: 'root' })
export class ScheduledReportService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scheduled-reports', 'reportingservice');

  create(scheduledReport: NewScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledReport);
    return this.http
      .post<RestScheduledReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(scheduledReport: IScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledReport);
    return this.http
      .put<RestScheduledReport>(`${this.resourceUrl}/${encodeURIComponent(this.getScheduledReportIdentifier(scheduledReport))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(scheduledReport: PartialUpdateScheduledReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scheduledReport);
    return this.http
      .patch<RestScheduledReport>(`${this.resourceUrl}/${encodeURIComponent(this.getScheduledReportIdentifier(scheduledReport))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestScheduledReport>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestScheduledReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getScheduledReportIdentifier(scheduledReport: Pick<IScheduledReport, 'id'>): number {
    return scheduledReport.id;
  }

  compareScheduledReport(o1: Pick<IScheduledReport, 'id'> | null, o2: Pick<IScheduledReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getScheduledReportIdentifier(o1) === this.getScheduledReportIdentifier(o2) : o1 === o2;
  }

  addScheduledReportToCollectionIfMissing<Type extends Pick<IScheduledReport, 'id'>>(
    scheduledReportCollection: Type[],
    ...scheduledReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scheduledReports: Type[] = scheduledReportsToCheck.filter(isPresent);
    if (scheduledReports.length > 0) {
      const scheduledReportCollectionIdentifiers = scheduledReportCollection.map(scheduledReportItem =>
        this.getScheduledReportIdentifier(scheduledReportItem),
      );
      const scheduledReportsToAdd = scheduledReports.filter(scheduledReportItem => {
        const scheduledReportIdentifier = this.getScheduledReportIdentifier(scheduledReportItem);
        if (scheduledReportCollectionIdentifiers.includes(scheduledReportIdentifier)) {
          return false;
        }
        scheduledReportCollectionIdentifiers.push(scheduledReportIdentifier);
        return true;
      });
      return [...scheduledReportsToAdd, ...scheduledReportCollection];
    }
    return scheduledReportCollection;
  }

  protected convertDateFromClient<T extends IScheduledReport | NewScheduledReport | PartialUpdateScheduledReport>(
    scheduledReport: T,
  ): RestOf<T> {
    return {
      ...scheduledReport,
      lastRun: scheduledReport.lastRun?.toJSON() ?? null,
      nextRun: scheduledReport.nextRun?.toJSON() ?? null,
      createdDate: scheduledReport.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restScheduledReport: RestScheduledReport): IScheduledReport {
    return {
      ...restScheduledReport,
      lastRun: restScheduledReport.lastRun ? dayjs(restScheduledReport.lastRun) : undefined,
      nextRun: restScheduledReport.nextRun ? dayjs(restScheduledReport.nextRun) : undefined,
      createdDate: restScheduledReport.createdDate ? dayjs(restScheduledReport.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestScheduledReport>): HttpResponse<IScheduledReport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestScheduledReport[]>): HttpResponse<IScheduledReport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
