import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportExecutionResolve from './route/report-execution-routing-resolve.service';

const reportExecutionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/report-execution.component').then(m => m.ReportExecutionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/report-execution-detail.component').then(m => m.ReportExecutionDetailComponent),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/report-execution-update.component').then(m => m.ReportExecutionUpdateComponent),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/report-execution-update.component').then(m => m.ReportExecutionUpdateComponent),
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportExecutionRoute;
