import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentVersionResolve from './route/document-version-routing-resolve.service';

const documentVersionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-version.component').then(m => m.DocumentVersionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-version-detail.component').then(m => m.DocumentVersionDetailComponent),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-version-update.component').then(m => m.DocumentVersionUpdateComponent),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-version-update.component').then(m => m.DocumentVersionUpdateComponent),
    resolve: {
      documentVersion: DocumentVersionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentVersionRoute;
