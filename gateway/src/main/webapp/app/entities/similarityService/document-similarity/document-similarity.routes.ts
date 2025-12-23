import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentSimilarityResolve from './route/document-similarity-routing-resolve.service';

const documentSimilarityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-similarity').then(m => m.DocumentSimilarity),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-similarity-detail').then(m => m.DocumentSimilarityDetail),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-similarity-update').then(m => m.DocumentSimilarityUpdate),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-similarity-update').then(m => m.DocumentSimilarityUpdate),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentSimilarityRoute;
