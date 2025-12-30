import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkflowTaskResolve from './route/workflow-task-routing-resolve.service';

const workflowTaskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-task.component').then(m => m.WorkflowTaskComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-task-detail.component').then(m => m.WorkflowTaskDetailComponent),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-task-update.component').then(m => m.WorkflowTaskUpdateComponent),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-task-update.component').then(m => m.WorkflowTaskUpdateComponent),
    resolve: {
      workflowTask: WorkflowTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowTaskRoute;
