import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScanBatchResolve from './route/scan-batch-routing-resolve.service';

const scanBatchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scan-batch').then(m => m.ScanBatch),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scan-batch-detail').then(m => m.ScanBatchDetail),
    resolve: {
      scanBatch: ScanBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scan-batch-update').then(m => m.ScanBatchUpdate),
    resolve: {
      scanBatch: ScanBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scan-batch-update').then(m => m.ScanBatchUpdate),
    resolve: {
      scanBatch: ScanBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scanBatchRoute;
