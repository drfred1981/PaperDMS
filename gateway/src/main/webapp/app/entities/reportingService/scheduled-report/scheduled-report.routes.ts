import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScheduledReportResolve from './route/scheduled-report-routing-resolve.service';

const scheduledReportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scheduled-report').then(m => m.ScheduledReport),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scheduled-report-detail').then(m => m.ScheduledReportDetail),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scheduled-report-update').then(m => m.ScheduledReportUpdate),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scheduled-report-update').then(m => m.ScheduledReportUpdate),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scheduledReportRoute;
