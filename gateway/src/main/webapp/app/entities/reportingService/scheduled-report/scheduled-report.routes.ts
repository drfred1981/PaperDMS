import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ScheduledReportResolve from './route/scheduled-report-routing-resolve.service';

const scheduledReportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scheduled-report.component').then(m => m.ScheduledReportComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scheduled-report-detail.component').then(m => m.ScheduledReportDetailComponent),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scheduled-report-update.component').then(m => m.ScheduledReportUpdateComponent),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scheduled-report-update.component').then(m => m.ScheduledReportUpdateComponent),
    resolve: {
      scheduledReport: ScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scheduledReportRoute;
