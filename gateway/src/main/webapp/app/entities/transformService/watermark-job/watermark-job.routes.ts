import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WatermarkJobResolve from './route/watermark-job-routing-resolve.service';

const watermarkJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/watermark-job').then(m => m.WatermarkJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/watermark-job-detail').then(m => m.WatermarkJobDetail),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/watermark-job-update').then(m => m.WatermarkJobUpdate),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/watermark-job-update').then(m => m.WatermarkJobUpdate),
    resolve: {
      watermarkJob: WatermarkJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default watermarkJobRoute;
