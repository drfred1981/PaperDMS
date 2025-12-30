import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingSystemMetricResolve from './route/reporting-system-metric-routing-resolve.service';

const reportingSystemMetricRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-system-metric.component').then(m => m.ReportingSystemMetricComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reporting-system-metric-detail.component').then(m => m.ReportingSystemMetricDetailComponent),
    resolve: {
      reportingSystemMetric: ReportingSystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reporting-system-metric-update.component').then(m => m.ReportingSystemMetricUpdateComponent),
    resolve: {
      reportingSystemMetric: ReportingSystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reporting-system-metric-update.component').then(m => m.ReportingSystemMetricUpdateComponent),
    resolve: {
      reportingSystemMetric: ReportingSystemMetricResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingSystemMetricRoute;
