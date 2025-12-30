import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingDashboardWidgetResolve from './route/reporting-dashboard-widget-routing-resolve.service';

const reportingDashboardWidgetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-dashboard-widget.component').then(m => m.ReportingDashboardWidgetComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/reporting-dashboard-widget-detail.component').then(m => m.ReportingDashboardWidgetDetailComponent),
    resolve: {
      reportingDashboardWidget: ReportingDashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/reporting-dashboard-widget-update.component').then(m => m.ReportingDashboardWidgetUpdateComponent),
    resolve: {
      reportingDashboardWidget: ReportingDashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/reporting-dashboard-widget-update.component').then(m => m.ReportingDashboardWidgetUpdateComponent),
    resolve: {
      reportingDashboardWidget: ReportingDashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingDashboardWidgetRoute;
