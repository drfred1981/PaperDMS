import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SystemMetricResolve from './route/system-metric-routing-resolve.service';

const systemMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-metric.component').then(m => m.SystemMetricComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-metric-detail.component').then(m => m.SystemMetricDetailComponent),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-metric-update.component').then(m => m.SystemMetricUpdateComponent),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-metric-update.component').then(m => m.SystemMetricUpdateComponent),
    resolve: {
      systemMetric: SystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default systemMetricRoute;
