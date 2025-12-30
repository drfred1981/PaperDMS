import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingDashboard, NewReportingDashboard } from '../reporting-dashboard.model';

export type PartialUpdateReportingDashboard = Partial<IReportingDashboard> & Pick<IReportingDashboard, 'id'>;

type RestOf<T extends IReportingDashboard | NewReportingDashboard> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestReportingDashboard = RestOf<IReportingDashboard>;

export type NewRestReportingDashboard = RestOf<NewReportingDashboard>;

export type PartialUpdateRestReportingDashboard = RestOf<PartialUpdateReportingDashboard>;

export type EntityResponseType = HttpResponse<IReportingDashboard>;
export type EntityArrayResponseType = HttpResponse<IReportingDashboard[]>;

@Injectable({ providedIn: 'root' })
export class ReportingDashboardService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-dashboards', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/reporting-dashboards/_search', 'reportingservice');

  create(reportingDashboard: NewReportingDashboard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingDashboard);
    return this.http
      .post<RestReportingDashboard>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportingDashboard: IReportingDashboard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingDashboard);
    return this.http
      .put<RestReportingDashboard>(`${this.resourceUrl}/${this.getReportingDashboardIdentifier(reportingDashboard)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportingDashboard: PartialUpdateReportingDashboard): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportingDashboard);
    return this.http
      .patch<RestReportingDashboard>(`${this.resourceUrl}/${this.getReportingDashboardIdentifier(reportingDashboard)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportingDashboard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportingDashboard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestReportingDashboard[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IReportingDashboard[]>()], asapScheduler)),
    );
  }

  getReportingDashboardIdentifier(reportingDashboard: Pick<IReportingDashboard, 'id'>): number {
    return reportingDashboard.id;
  }

  compareReportingDashboard(o1: Pick<IReportingDashboard, 'id'> | null, o2: Pick<IReportingDashboard, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportingDashboardIdentifier(o1) === this.getReportingDashboardIdentifier(o2) : o1 === o2;
  }

  addReportingDashboardToCollectionIfMissing<Type extends Pick<IReportingDashboard, 'id'>>(
    reportingDashboardCollection: Type[],
    ...reportingDashboardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingDashboards: Type[] = reportingDashboardsToCheck.filter(isPresent);
    if (reportingDashboards.length > 0) {
      const reportingDashboardCollectionIdentifiers = reportingDashboardCollection.map(reportingDashboardItem =>
        this.getReportingDashboardIdentifier(reportingDashboardItem),
      );
      const reportingDashboardsToAdd = reportingDashboards.filter(reportingDashboardItem => {
        const reportingDashboardIdentifier = this.getReportingDashboardIdentifier(reportingDashboardItem);
        if (reportingDashboardCollectionIdentifiers.includes(reportingDashboardIdentifier)) {
          return false;
        }
        reportingDashboardCollectionIdentifiers.push(reportingDashboardIdentifier);
        return true;
      });
      return [...reportingDashboardsToAdd, ...reportingDashboardCollection];
    }
    return reportingDashboardCollection;
  }

  protected convertDateFromClient<T extends IReportingDashboard | NewReportingDashboard | PartialUpdateReportingDashboard>(
    reportingDashboard: T,
  ): RestOf<T> {
    return {
      ...reportingDashboard,
      createdDate: reportingDashboard.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReportingDashboard: RestReportingDashboard): IReportingDashboard {
    return {
      ...restReportingDashboard,
      createdDate: restReportingDashboard.createdDate ? dayjs(restReportingDashboard.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportingDashboard>): HttpResponse<IReportingDashboard> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportingDashboard[]>): HttpResponse<IReportingDashboard[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
