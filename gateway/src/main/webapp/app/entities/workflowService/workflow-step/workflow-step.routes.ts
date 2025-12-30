import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkflowStepResolve from './route/workflow-step-routing-resolve.service';

const workflowStepRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-step.component').then(m => m.WorkflowStepComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-step-detail.component').then(m => m.WorkflowStepDetailComponent),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-step-update.component').then(m => m.WorkflowStepUpdateComponent),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-step-update.component').then(m => m.WorkflowStepUpdateComponent),
    resolve: {
      workflowStep: WorkflowStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowStepRoute;
