import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentFingerprintResolve from './route/document-fingerprint-routing-resolve.service';

const documentFingerprintRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-fingerprint').then(m => m.DocumentFingerprint),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-fingerprint-detail').then(m => m.DocumentFingerprintDetail),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-fingerprint-update').then(m => m.DocumentFingerprintUpdate),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-fingerprint-update').then(m => m.DocumentFingerprintUpdate),
    resolve: {
      documentFingerprint: DocumentFingerprintResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentFingerprintRoute;
