import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportingExecutionResolve from './route/reporting-execution-routing-resolve.service';

const reportingExecutionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reporting-execution.component').then(m => m.ReportingExecutionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reporting-execution-detail.component').then(m => m.ReportingExecutionDetailComponent),
    resolve: {
      reportingExecution: ReportingExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reporting-execution-update.component').then(m => m.ReportingExecutionUpdateComponent),
    resolve: {
      reportingExecution: ReportingExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reporting-execution-update.component').then(m => m.ReportingExecutionUpdateComponent),
    resolve: {
      reportingExecution: ReportingExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportingExecutionRoute;
