import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentVersionResolve from './route/document-version-routing-resolve.service';

const documentVersionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-version').then(m => m.DocumentVersion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-version-detail').then(m => m.DocumentVersionDetail),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-version-update').then(m => m.DocumentVersionUpdate),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-version-update').then(m => m.DocumentVersionUpdate),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentVersionRoute;
