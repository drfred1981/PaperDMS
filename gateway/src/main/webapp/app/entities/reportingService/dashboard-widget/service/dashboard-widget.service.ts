import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDashboardWidget, NewDashboardWidget } from '../dashboard-widget.model';

export type PartialUpdateDashboardWidget = Partial<IDashboardWidget> & Pick<IDashboardWidget, 'id'>;

export type EntityResponseType = HttpResponse<IDashboardWidget>;
export type EntityArrayResponseType = HttpResponse<IDashboardWidget[]>;

@Injectable({ providedIn: 'root' })
export class DashboardWidgetService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboard-widgets', 'reportingservice');

  create(dashboardWidget: NewDashboardWidget): Observable<EntityResponseType> {
    return this.http.post<IDashboardWidget>(this.resourceUrl, dashboardWidget, { observe: 'response' });
  }

  update(dashboardWidget: IDashboardWidget): Observable<EntityResponseType> {
    return this.http.put<IDashboardWidget>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDashboardWidgetIdentifier(dashboardWidget))}`,
      dashboardWidget,
      { observe: 'response' },
    );
  }

  partialUpdate(dashboardWidget: PartialUpdateDashboardWidget): Observable<EntityResponseType> {
    return this.http.patch<IDashboardWidget>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDashboardWidgetIdentifier(dashboardWidget))}`,
      dashboardWidget,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDashboardWidget>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDashboardWidget[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDashboardWidgetIdentifier(dashboardWidget: Pick<IDashboardWidget, 'id'>): number {
    return dashboardWidget.id;
  }

  compareDashboardWidget(o1: Pick<IDashboardWidget, 'id'> | null, o2: Pick<IDashboardWidget, 'id'> | null): boolean {
    return o1 && o2 ? this.getDashboardWidgetIdentifier(o1) === this.getDashboardWidgetIdentifier(o2) : o1 === o2;
  }

  addDashboardWidgetToCollectionIfMissing<Type extends Pick<IDashboardWidget, 'id'>>(
    dashboardWidgetCollection: Type[],
    ...dashboardWidgetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dashboardWidgets: Type[] = dashboardWidgetsToCheck.filter(isPresent);
    if (dashboardWidgets.length > 0) {
      const dashboardWidgetCollectionIdentifiers = dashboardWidgetCollection.map(dashboardWidgetItem =>
        this.getDashboardWidgetIdentifier(dashboardWidgetItem),
      );
      const dashboardWidgetsToAdd = dashboardWidgets.filter(dashboardWidgetItem => {
        const dashboardWidgetIdentifier = this.getDashboardWidgetIdentifier(dashboardWidgetItem);
        if (dashboardWidgetCollectionIdentifiers.includes(dashboardWidgetIdentifier)) {
          return false;
        }
        dashboardWidgetCollectionIdentifiers.push(dashboardWidgetIdentifier);
        return true;
      });
      return [...dashboardWidgetsToAdd, ...dashboardWidgetCollection];
    }
    return dashboardWidgetCollection;
  }
}
