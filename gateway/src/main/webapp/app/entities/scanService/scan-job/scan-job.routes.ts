import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScanJobResolve from './route/scan-job-routing-resolve.service';

const scanJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scan-job').then(m => m.ScanJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scan-job-detail').then(m => m.ScanJobDetail),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scan-job-update').then(m => m.ScanJobUpdate),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scan-job-update').then(m => m.ScanJobUpdate),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scanJobRoute;
