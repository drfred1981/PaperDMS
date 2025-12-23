import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PerformanceMetricResolve from './route/performance-metric-routing-resolve.service';

const performanceMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/performance-metric').then(m => m.PerformanceMetric),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/performance-metric-detail').then(m => m.PerformanceMetricDetail),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/performance-metric-update').then(m => m.PerformanceMetricUpdate),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/performance-metric-update').then(m => m.PerformanceMetricUpdate),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default performanceMetricRoute;
