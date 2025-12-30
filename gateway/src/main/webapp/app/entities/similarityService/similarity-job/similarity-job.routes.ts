import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SimilarityJobResolve from './route/similarity-job-routing-resolve.service';

const similarityJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-job.component').then(m => m.SimilarityJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/similarity-job-detail.component').then(m => m.SimilarityJobDetailComponent),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/similarity-job-update.component').then(m => m.SimilarityJobUpdateComponent),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/similarity-job-update.component').then(m => m.SimilarityJobUpdateComponent),
    resolve: {
      similarityJob: SimilarityJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityJobRoute;
