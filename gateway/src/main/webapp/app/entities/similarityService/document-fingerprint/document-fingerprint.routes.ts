import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentFingerprintResolve from './route/document-fingerprint-routing-resolve.service';

const documentFingerprintRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-fingerprint.component').then(m => m.DocumentFingerprintComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-fingerprint-detail.component').then(m => m.DocumentFingerprintDetailComponent),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-fingerprint-update.component').then(m => m.DocumentFingerprintUpdateComponent),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-fingerprint-update.component').then(m => m.DocumentFingerprintUpdateComponent),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentFingerprintRoute;
