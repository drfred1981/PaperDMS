import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingExecution, NewReportingExecution } from '../reporting-execution.model';

export type PartialUpdateReportingExecution = Partial<IReportingExecution> & Pick<IReportingExecution, 'id'>;

type RestOf<T extends IReportingExecution | NewReportingExecution> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestReportingExecution = RestOf<IReportingExecution>;

export type NewRestReportingExecution = RestOf<NewReportingExecution>;

export type PartialUpdateRestReportingExecution = RestOf<PartialUpdateReportingExecution>;

export type EntityResponseType = HttpResponse<IReportingExecution>;
export type EntityArrayResponseType = HttpResponse<IReportingExecution[]>;

@Injectable({ providedIn: 'root' })
export class ReportingExecutionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-executions', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/reporting-executions/_search', 'reportingservice');

  create(reportingExecution: NewReportingExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingExecution);
    return this.http
      .post<RestReportingExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportingExecution: IReportingExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingExecution);
    return this.http
      .put<RestReportingExecution>(`${this.resourceUrl}/${this.getReportingExecutionIdentifier(reportingExecution)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportingExecution: PartialUpdateReportingExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingExecution);
    return this.http
      .patch<RestReportingExecution>(`${this.resourceUrl}/${this.getReportingExecutionIdentifier(reportingExecution)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportingExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportingExecution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportingExecution[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportingExecution[]>()], asapScheduler)),
    );
  }

  getReportingExecutionIdentifier(reportingExecution: Pick<IReportingExecution, 'id'>): number {
    return reportingExecution.id;
  }

  compareReportingExecution(o1: Pick<IReportingExecution, 'id'> | null, o2: Pick<IReportingExecution, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportingExecutionIdentifier(o1) === this.getReportingExecutionIdentifier(o2) : o1 === o2;
  }

  addReportingExecutionToCollectionIfMissing<Type extends Pick<IReportingExecution, 'id'>>(
    reportingExecutionCollection: Type[],
    ...reportingExecutionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingExecutions: Type[] = reportingExecutionsToCheck.filter(isPresent);
    if (reportingExecutions.length > 0) {
      const reportingExecutionCollectionIdentifiers = reportingExecutionCollection.map(reportingExecutionItem =>
        this.getReportingExecutionIdentifier(reportingExecutionItem),
      );
      const reportingExecutionsToAdd = reportingExecutions.filter(reportingExecutionItem => {
        const reportingExecutionIdentifier = this.getReportingExecutionIdentifier(reportingExecutionItem);
        if (reportingExecutionCollectionIdentifiers.includes(reportingExecutionIdentifier)) {
          return false;
        }
        reportingExecutionCollectionIdentifiers.push(reportingExecutionIdentifier);
        return true;
      });
      return [...reportingExecutionsToAdd, ...reportingExecutionCollection];
    }
    return reportingExecutionCollection;
  }

  protected convertDateFromClient<T extends IReportingExecution | NewReportingExecution | PartialUpdateReportingExecution>(
    reportingExecution: T,
  ): RestOf<T> {
    return {
      ...reportingExecution,
      startDate: reportingExecution.startDate?.toJSON() ?? null,
      endDate: reportingExecution.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportingExecution: RestReportingExecution): IReportingExecution {
    return {
      ...restReportingExecution,
      startDate: restReportingExecution.startDate ? dayjs(restReportingExecution.startDate) : undefined,
      endDate: restReportingExecution.endDate ? dayjs(restReportingExecution.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportingExecution>): HttpResponse<IReportingExecution> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportingExecution[]>): HttpResponse<IReportingExecution[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
