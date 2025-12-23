import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ComparisonJobResolve from './route/comparison-job-routing-resolve.service';

const comparisonJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/comparison-job').then(m => m.ComparisonJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/comparison-job-detail').then(m => m.ComparisonJobDetail),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/comparison-job-update').then(m => m.ComparisonJobUpdate),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/comparison-job-update').then(m => m.ComparisonJobUpdate),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default comparisonJobRoute;
