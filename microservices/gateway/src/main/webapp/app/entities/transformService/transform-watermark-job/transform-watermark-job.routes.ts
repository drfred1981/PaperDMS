import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransformWatermarkJobResolve from './route/transform-watermark-job-routing-resolve.service';

const transformWatermarkJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transform-watermark-job.component').then(m => m.TransformWatermarkJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transform-watermark-job-detail.component').then(m => m.TransformWatermarkJobDetailComponent),
    resolve: {
      transformWatermarkJob: TransformWatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transform-watermark-job-update.component').then(m => m.TransformWatermarkJobUpdateComponent),
    resolve: {
      transformWatermarkJob: TransformWatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transform-watermark-job-update.component').then(m => m.TransformWatermarkJobUpdateComponent),
    resolve: {
      transformWatermarkJob: TransformWatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transformWatermarkJobRoute;
