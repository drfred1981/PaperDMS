import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SimilarityJobResolve from './route/similarity-job-routing-resolve.service';

const similarityJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-job').then(m => m.SimilarityJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/similarity-job-detail').then(m => m.SimilarityJobDetail),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/similarity-job-update').then(m => m.SimilarityJobUpdate),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/similarity-job-update').then(m => m.SimilarityJobUpdate),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityJobRoute;
