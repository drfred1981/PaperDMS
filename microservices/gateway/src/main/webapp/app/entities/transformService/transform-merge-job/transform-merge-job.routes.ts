import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransformMergeJobResolve from './route/transform-merge-job-routing-resolve.service';

const transformMergeJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transform-merge-job.component').then(m => m.TransformMergeJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transform-merge-job-detail.component').then(m => m.TransformMergeJobDetailComponent),
    resolve: {
      transformMergeJob: TransformMergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transform-merge-job-update.component').then(m => m.TransformMergeJobUpdateComponent),
    resolve: {
      transformMergeJob: TransformMergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transform-merge-job-update.component').then(m => m.TransformMergeJobUpdateComponent),
    resolve: {
      transformMergeJob: TransformMergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transformMergeJobRoute;
