import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DashboardResolve from './route/dashboard-routing-resolve.service';

const dashboardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dashboard').then(m => m.Dashboard),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dashboard-detail').then(m => m.DashboardDetail),
    resolve: {
      dashboard: DashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dashboard-update').then(m => m.DashboardUpdate),
    resolve: {
      dashboard: DashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dashboard-update').then(m => m.DashboardUpdate),
    resolve: {
      dashboard: DashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dashboardRoute;
