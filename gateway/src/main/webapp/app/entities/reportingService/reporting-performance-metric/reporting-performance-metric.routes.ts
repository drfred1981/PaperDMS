import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingPerformanceMetricResolve from './route/reporting-performance-metric-routing-resolve.service';

const reportingPerformanceMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-performance-metric.component').then(m => m.ReportingPerformanceMetricComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/reporting-performance-metric-detail.component').then(m => m.ReportingPerformanceMetricDetailComponent),
    resolve: {
      reportingPerformanceMetric: ReportingPerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/reporting-performance-metric-update.component').then(m => m.ReportingPerformanceMetricUpdateComponent),
    resolve: {
      reportingPerformanceMetric: ReportingPerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/reporting-performance-metric-update.component').then(m => m.ReportingPerformanceMetricUpdateComponent),
    resolve: {
      reportingPerformanceMetric: ReportingPerformanceMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingPerformanceMetricRoute;
