import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PerformanceMetricResolve from './route/performance-metric-routing-resolve.service';

const performanceMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/performance-metric.component').then(m => m.PerformanceMetricComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/performance-metric-detail.component').then(m => m.PerformanceMetricDetailComponent),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/performance-metric-update.component').then(m => m.PerformanceMetricUpdateComponent),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/performance-metric-update.component').then(m => m.PerformanceMetricUpdateComponent),
    resolve: {
      performanceMetric: PerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default performanceMetricRoute;
