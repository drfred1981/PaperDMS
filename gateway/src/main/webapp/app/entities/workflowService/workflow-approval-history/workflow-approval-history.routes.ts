import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorkflowApprovalHistoryResolve from './route/workflow-approval-history-routing-resolve.service';

const workflowApprovalHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/workflow-approval-history.component').then(m => m.WorkflowApprovalHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/workflow-approval-history-detail.component').then(m => m.WorkflowApprovalHistoryDetailComponent),
    resolve: {
      workflowApprovalHistory: WorkflowApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/workflow-approval-history-update.component').then(m => m.WorkflowApprovalHistoryUpdateComponent),
    resolve: {
      workflowApprovalHistory: WorkflowApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/workflow-approval-history-update.component').then(m => m.WorkflowApprovalHistoryUpdateComponent),
    resolve: {
      workflowApprovalHistory: WorkflowApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workflowApprovalHistoryRoute;
