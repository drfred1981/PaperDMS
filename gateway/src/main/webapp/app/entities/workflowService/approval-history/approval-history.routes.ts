import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ApprovalHistoryResolve from './route/approval-history-routing-resolve.service';

const approvalHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/approval-history').then(m => m.ApprovalHistory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/approval-history-detail').then(m => m.ApprovalHistoryDetail),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/approval-history-update').then(m => m.ApprovalHistoryUpdate),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/approval-history-update').then(m => m.ApprovalHistoryUpdate),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default approvalHistoryRoute;
