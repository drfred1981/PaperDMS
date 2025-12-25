import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WatermarkJobResolve from './route/watermark-job-routing-resolve.service';

const watermarkJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/watermark-job.component').then(m => m.WatermarkJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/watermark-job-detail.component').then(m => m.WatermarkJobDetailComponent),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/watermark-job-update.component').then(m => m.WatermarkJobUpdateComponent),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/watermark-job-update.component').then(m => m.WatermarkJobUpdateComponent),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default watermarkJobRoute;
