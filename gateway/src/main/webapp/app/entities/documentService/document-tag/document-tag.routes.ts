import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentTagResolve from './route/document-tag-routing-resolve.service';

const documentTagRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-tag').then(m => m.DocumentTag),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-tag-detail').then(m => m.DocumentTagDetail),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-tag-update').then(m => m.DocumentTagUpdate),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-tag-update').then(m => m.DocumentTagUpdate),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTagRoute;
