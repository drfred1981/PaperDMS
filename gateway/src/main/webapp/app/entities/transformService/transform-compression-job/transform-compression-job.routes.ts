import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransformCompressionJobResolve from './route/transform-compression-job-routing-resolve.service';

const transformCompressionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transform-compression-job.component').then(m => m.TransformCompressionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transform-compression-job-detail.component').then(m => m.TransformCompressionJobDetailComponent),
    resolve: {
      transformCompressionJob: TransformCompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transform-compression-job-update.component').then(m => m.TransformCompressionJobUpdateComponent),
    resolve: {
      transformCompressionJob: TransformCompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transform-compression-job-update.component').then(m => m.TransformCompressionJobUpdateComponent),
    resolve: {
      transformCompressionJob: TransformCompressionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transformCompressionJobRoute;
