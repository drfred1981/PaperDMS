import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SimilarityDocumentComparisonResolve from './route/similarity-document-comparison-routing-resolve.service';

const similarityDocumentComparisonRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-document-comparison.component').then(m => m.SimilarityDocumentComparisonComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/similarity-document-comparison-detail.component').then(m => m.SimilarityDocumentComparisonDetailComponent),
    resolve: {
      similarityDocumentComparison: SimilarityDocumentComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/similarity-document-comparison-update.component').then(m => m.SimilarityDocumentComparisonUpdateComponent),
    resolve: {
      similarityDocumentComparison: SimilarityDocumentComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/similarity-document-comparison-update.component').then(m => m.SimilarityDocumentComparisonUpdateComponent),
    resolve: {
      similarityDocumentComparison: SimilarityDocumentComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityDocumentComparisonRoute;
