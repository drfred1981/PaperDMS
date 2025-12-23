import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import AutoTagJobResolve from './route/auto-tag-job-routing-resolve.service';

const autoTagJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/auto-tag-job').then(m => m.AutoTagJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/auto-tag-job-detail').then(m => m.AutoTagJobDetail),
    resolve: {
      autoTagJob: AutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/auto-tag-job-update').then(m => m.AutoTagJobUpdate),
    resolve: {
      autoTagJob: AutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/auto-tag-job-update').then(m => m.AutoTagJobUpdate),
    resolve: {
      autoTagJob: AutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default autoTagJobRoute;
