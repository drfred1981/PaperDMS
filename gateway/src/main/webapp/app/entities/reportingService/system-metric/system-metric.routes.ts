import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SystemMetricResolve from './route/system-metric-routing-resolve.service';

const systemMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-metric').then(m => m.SystemMetric),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-metric-detail').then(m => m.SystemMetricDetail),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-metric-update').then(m => m.SystemMetricUpdate),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-metric-update').then(m => m.SystemMetricUpdate),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default systemMetricRoute;
