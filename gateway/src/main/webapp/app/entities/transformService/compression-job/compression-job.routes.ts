import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CompressionJobResolve from './route/compression-job-routing-resolve.service';

const compressionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compression-job.component').then(m => m.CompressionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compression-job-detail.component').then(m => m.CompressionJobDetailComponent),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compression-job-update.component').then(m => m.CompressionJobUpdateComponent),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compression-job-update.component').then(m => m.CompressionJobUpdateComponent),
    resolve: {
      compressionJob: CompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compressionJobRoute;
