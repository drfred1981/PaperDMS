import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WorkflowTaskResolve from './route/workflow-task-routing-resolve.service';

const workflowTaskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-task').then(m => m.WorkflowTask),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-task-detail').then(m => m.WorkflowTaskDetail),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-task-update').then(m => m.WorkflowTaskUpdate),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-task-update').then(m => m.WorkflowTaskUpdate),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowTaskRoute;
