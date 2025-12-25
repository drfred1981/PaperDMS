import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ComparisonJobResolve from './route/comparison-job-routing-resolve.service';

const comparisonJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/comparison-job.component').then(m => m.ComparisonJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/comparison-job-detail.component').then(m => m.ComparisonJobDetailComponent),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/comparison-job-update.component').then(m => m.ComparisonJobUpdateComponent),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/comparison-job-update.component').then(m => m.ComparisonJobUpdateComponent),
    resolve: {
      comparisonJob: ComparisonJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default comparisonJobRoute;
