import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WorkflowStepResolve from './route/workflow-step-routing-resolve.service';

const workflowStepRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-step').then(m => m.WorkflowStep),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-step-detail').then(m => m.WorkflowStepDetail),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-step-update').then(m => m.WorkflowStepUpdate),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-step-update').then(m => m.WorkflowStepUpdate),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowStepRoute;
