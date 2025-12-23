import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SimilarityClusterResolve from './route/similarity-cluster-routing-resolve.service';

const similarityClusterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-cluster').then(m => m.SimilarityCluster),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/similarity-cluster-detail').then(m => m.SimilarityClusterDetail),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/similarity-cluster-update').then(m => m.SimilarityClusterUpdate),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/similarity-cluster-update').then(m => m.SimilarityClusterUpdate),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityClusterRoute;
