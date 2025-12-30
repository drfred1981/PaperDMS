import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingScheduledReportResolve from './route/reporting-scheduled-report-routing-resolve.service';

const reportingScheduledReportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-scheduled-report.component').then(m => m.ReportingScheduledReportComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/reporting-scheduled-report-detail.component').then(m => m.ReportingScheduledReportDetailComponent),
    resolve: {
      reportingScheduledReport: ReportingScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/reporting-scheduled-report-update.component').then(m => m.ReportingScheduledReportUpdateComponent),
    resolve: {
      reportingScheduledReport: ReportingScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/reporting-scheduled-report-update.component').then(m => m.ReportingScheduledReportUpdateComponent),
    resolve: {
      reportingScheduledReport: ReportingScheduledReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingScheduledReportRoute;
