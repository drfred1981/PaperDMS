import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ApprovalHistoryResolve from './route/approval-history-routing-resolve.service';

const approvalHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/approval-history.component').then(m => m.ApprovalHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/approval-history-detail.component').then(m => m.ApprovalHistoryDetailComponent),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/approval-history-update.component').then(m => m.ApprovalHistoryUpdateComponent),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/approval-history-update.component').then(m => m.ApprovalHistoryUpdateComponent),
    resolve: {
      approvalHistory: ApprovalHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default approvalHistoryRoute;
