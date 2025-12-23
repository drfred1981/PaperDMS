import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ReportExecutionResolve from './route/report-execution-routing-resolve.service';

const reportExecutionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/report-execution').then(m => m.ReportExecution),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/report-execution-detail').then(m => m.ReportExecutionDetail),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/report-execution-update').then(m => m.ReportExecutionUpdate),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/report-execution-update').then(m => m.ReportExecutionUpdate),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportExecutionRoute;
