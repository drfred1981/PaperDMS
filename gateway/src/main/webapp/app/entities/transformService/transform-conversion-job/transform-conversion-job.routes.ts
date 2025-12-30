import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransformConversionJobResolve from './route/transform-conversion-job-routing-resolve.service';

const transformConversionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transform-conversion-job.component').then(m => m.TransformConversionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transform-conversion-job-detail.component').then(m => m.TransformConversionJobDetailComponent),
    resolve: {
      transformConversionJob: TransformConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transform-conversion-job-update.component').then(m => m.TransformConversionJobUpdateComponent),
    resolve: {
      transformConversionJob: TransformConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transform-conversion-job-update.component').then(m => m.TransformConversionJobUpdateComponent),
    resolve: {
      transformConversionJob: TransformConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transformConversionJobRoute;
