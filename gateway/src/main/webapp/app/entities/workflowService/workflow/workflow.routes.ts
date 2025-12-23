import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WorkflowResolve from './route/workflow-routing-resolve.service';

const workflowRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow').then(m => m.Workflow),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-detail').then(m => m.WorkflowDetail),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-update').then(m => m.WorkflowUpdate),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-update').then(m => m.WorkflowUpdate),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowRoute;
