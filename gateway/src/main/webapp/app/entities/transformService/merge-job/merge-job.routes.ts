import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MergeJobResolve from './route/merge-job-routing-resolve.service';

const mergeJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/merge-job.component').then(m => m.MergeJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/merge-job-detail.component').then(m => m.MergeJobDetailComponent),
    resolve: {
      mergeJob: MergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/merge-job-update.component').then(m => m.MergeJobUpdateComponent),
    resolve: {
      mergeJob: MergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/merge-job-update.component').then(m => m.MergeJobUpdateComponent),
    resolve: {
      mergeJob: MergeJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mergeJobRoute;
