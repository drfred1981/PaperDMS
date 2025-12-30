import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingDashboardWidget, NewReportingDashboardWidget } from '../reporting-dashboard-widget.model';

export type PartialUpdateReportingDashboardWidget = Partial<IReportingDashboardWidget> & Pick<IReportingDashboardWidget, 'id'>;

export type EntityResponseType = HttpResponse<IReportingDashboardWidget>;
export type EntityArrayResponseType = HttpResponse<IReportingDashboardWidget[]>;

@Injectable({ providedIn: 'root' })
export class ReportingDashboardWidgetService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-dashboard-widgets', 'reportingservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/reporting-dashboard-widgets/_search', 'reportingservice');

  create(reportingDashboardWidget: NewReportingDashboardWidget): Observable<EntityResponseType> {
    return this.http.post<IReportingDashboardWidget>(this.resourceUrl, reportingDashboardWidget, { observe: 'response' });
  }

  update(reportingDashboardWidget: IReportingDashboardWidget): Observable<EntityResponseType> {
    return this.http.put<IReportingDashboardWidget>(
      `${this.resourceUrl}/${this.getReportingDashboardWidgetIdentifier(reportingDashboardWidget)}`,
      reportingDashboardWidget,
      { observe: 'response' },
    );
  }

  partialUpdate(reportingDashboardWidget: PartialUpdateReportingDashboardWidget): Observable<EntityResponseType> {
    return this.http.patch<IReportingDashboardWidget>(
      `${this.resourceUrl}/${this.getReportingDashboardWidgetIdentifier(reportingDashboardWidget)}`,
      reportingDashboardWidget,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportingDashboardWidget>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportingDashboardWidget[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReportingDashboardWidget[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IReportingDashboardWidget[]>()], asapScheduler)));
  }

  getReportingDashboardWidgetIdentifier(reportingDashboardWidget: Pick<IReportingDashboardWidget, 'id'>): number {
    return reportingDashboardWidget.id;
  }

  compareReportingDashboardWidget(
    o1: Pick<IReportingDashboardWidget, 'id'> | null,
    o2: Pick<IReportingDashboardWidget, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getReportingDashboardWidgetIdentifier(o1) === this.getReportingDashboardWidgetIdentifier(o2) : o1 === o2;
  }

  addReportingDashboardWidgetToCollectionIfMissing<Type extends Pick<IReportingDashboardWidget, 'id'>>(
    reportingDashboardWidgetCollection: Type[],
    ...reportingDashboardWidgetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportingDashboardWidgets: Type[] = reportingDashboardWidgetsToCheck.filter(isPresent);
    if (reportingDashboardWidgets.length > 0) {
      const reportingDashboardWidgetCollectionIdentifiers = reportingDashboardWidgetCollection.map(reportingDashboardWidgetItem =>
        this.getReportingDashboardWidgetIdentifier(reportingDashboardWidgetItem),
      );
      const reportingDashboardWidgetsToAdd = reportingDashboardWidgets.filter(reportingDashboardWidgetItem => {
        const reportingDashboardWidgetIdentifier = this.getReportingDashboardWidgetIdentifier(reportingDashboardWidgetItem);
        if (reportingDashboardWidgetCollectionIdentifiers.includes(reportingDashboardWidgetIdentifier)) {
          return false;
        }
        reportingDashboardWidgetCollectionIdentifiers.push(reportingDashboardWidgetIdentifier);
        return true;
      });
      return [...reportingDashboardWidgetsToAdd, ...reportingDashboardWidgetCollection];
    }
    return reportingDashboardWidgetCollection;
  }
}
