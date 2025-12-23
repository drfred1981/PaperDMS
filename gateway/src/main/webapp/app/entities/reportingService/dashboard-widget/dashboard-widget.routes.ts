import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DashboardWidgetResolve from './route/dashboard-widget-routing-resolve.service';

const dashboardWidgetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dashboard-widget').then(m => m.DashboardWidget),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dashboard-widget-detail').then(m => m.DashboardWidgetDetail),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dashboard-widget-update').then(m => m.DashboardWidgetUpdate),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dashboard-widget-update').then(m => m.DashboardWidgetUpdate),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dashboardWidgetRoute;
