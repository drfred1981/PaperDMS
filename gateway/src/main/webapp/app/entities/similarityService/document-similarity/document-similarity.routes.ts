import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentSimilarityResolve from './route/document-similarity-routing-resolve.service';

const documentSimilarityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-similarity.component').then(m => m.DocumentSimilarityComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-similarity-detail.component').then(m => m.DocumentSimilarityDetailComponent),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-similarity-update.component').then(m => m.DocumentSimilarityUpdateComponent),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-similarity-update.component').then(m => m.DocumentSimilarityUpdateComponent),
    resolve: {
      documentSimilarity: DocumentSimilarityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentSimilarityRoute;
