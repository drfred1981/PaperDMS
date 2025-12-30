import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkflowResolve from './route/workflow-routing-resolve.service';

const workflowRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow.component').then(m => m.WorkflowComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-detail.component').then(m => m.WorkflowDetailComponent),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-update.component').then(m => m.WorkflowUpdateComponent),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-update.component').then(m => m.WorkflowUpdateComponent),
    resolve: {
      workflow: WorkflowResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowRoute;
