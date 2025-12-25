import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkflowInstanceResolve from './route/workflow-instance-routing-resolve.service';

const workflowInstanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-instance.component').then(m => m.WorkflowInstanceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-instance-detail.component').then(m => m.WorkflowInstanceDetailComponent),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-instance-update.component').then(m => m.WorkflowInstanceUpdateComponent),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-instance-update.component').then(m => m.WorkflowInstanceUpdateComponent),
    resolve: {
      workflowInstance: WorkflowInstanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowInstanceRoute;
