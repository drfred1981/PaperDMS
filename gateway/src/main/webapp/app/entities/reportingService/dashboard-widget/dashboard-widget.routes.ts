import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DashboardWidgetResolve from './route/dashboard-widget-routing-resolve.service';

const dashboardWidgetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dashboard-widget.component').then(m => m.DashboardWidgetComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dashboard-widget-detail.component').then(m => m.DashboardWidgetDetailComponent),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dashboard-widget-update.component').then(m => m.DashboardWidgetUpdateComponent),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dashboard-widget-update.component').then(m => m.DashboardWidgetUpdateComponent),
    resolve: {
      dashboardWidget: DashboardWidgetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dashboardWidgetRoute;
