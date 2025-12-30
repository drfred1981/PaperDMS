import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingDashboardResolve from './route/reporting-dashboard-routing-resolve.service';

const reportingDashboardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-dashboard.component').then(m => m.ReportingDashboardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reporting-dashboard-detail.component').then(m => m.ReportingDashboardDetailComponent),
    resolve: {
      reportingDashboard: ReportingDashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reporting-dashboard-update.component').then(m => m.ReportingDashboardUpdateComponent),
    resolve: {
      reportingDashboard: ReportingDashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reporting-dashboard-update.component').then(m => m.ReportingDashboardUpdateComponent),
    resolve: {
      reportingDashboard: ReportingDashboardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingDashboardRoute;
