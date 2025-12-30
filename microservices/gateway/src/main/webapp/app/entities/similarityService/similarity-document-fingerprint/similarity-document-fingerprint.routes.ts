import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SimilarityDocumentFingerprintResolve from './route/similarity-document-fingerprint-routing-resolve.service';

const similarityDocumentFingerprintRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/similarity-document-fingerprint.component').then(m => m.SimilarityDocumentFingerprintComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/similarity-document-fingerprint-detail.component').then(m => m.SimilarityDocumentFingerprintDetailComponent),
    resolve: {
      similarityDocumentFingerprint: SimilarityDocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/similarity-document-fingerprint-update.component').then(m => m.SimilarityDocumentFingerprintUpdateComponent),
    resolve: {
      similarityDocumentFingerprint: SimilarityDocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/similarity-document-fingerprint-update.component').then(m => m.SimilarityDocumentFingerprintUpdateComponent),
    resolve: {
      similarityDocumentFingerprint: SimilarityDocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default similarityDocumentFingerprintRoute;
