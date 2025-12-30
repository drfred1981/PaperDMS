import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SimilarityClusterResolve from './route/similarity-cluster-routing-resolve.service';

const similarityClusterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-cluster.component').then(m => m.SimilarityClusterComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/similarity-cluster-detail.component').then(m => m.SimilarityClusterDetailComponent),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/similarity-cluster-update.component').then(m => m.SimilarityClusterUpdateComponent),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/similarity-cluster-update.component').then(m => m.SimilarityClusterUpdateComponent),
    resolve: {
      similarityCluster: SimilarityClusterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityClusterRoute;
