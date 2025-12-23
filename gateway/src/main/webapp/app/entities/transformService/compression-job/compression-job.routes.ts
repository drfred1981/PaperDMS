import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CompressionJobResolve from './route/compression-job-routing-resolve.service';

const compressionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compression-job').then(m => m.CompressionJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compression-job-detail').then(m => m.CompressionJobDetail),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compression-job-update').then(m => m.CompressionJobUpdate),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compression-job-update').then(m => m.CompressionJobUpdate),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compressionJobRoute;
