import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ScanJobResolve from './route/scan-job-routing-resolve.service';

const scanJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scan-job.component').then(m => m.ScanJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scan-job-detail.component').then(m => m.ScanJobDetailComponent),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scan-job-update.component').then(m => m.ScanJobUpdateComponent),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scan-job-update.component').then(m => m.ScanJobUpdateComponent),
    resolve: {
      scanJob: ScanJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scanJobRoute;
