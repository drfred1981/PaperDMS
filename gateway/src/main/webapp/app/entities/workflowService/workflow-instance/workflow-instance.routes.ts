import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WorkflowInstanceResolve from './route/workflow-instance-routing-resolve.service';

const workflowInstanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-instance').then(m => m.WorkflowInstance),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-instance-detail').then(m => m.WorkflowInstanceDetail),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-instance-update').then(m => m.WorkflowInstanceUpdate),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-instance-update').then(m => m.WorkflowInstanceUpdate),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowInstanceRoute;
