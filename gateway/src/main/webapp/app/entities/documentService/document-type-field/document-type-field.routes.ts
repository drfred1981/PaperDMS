import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentTypeFieldResolve from './route/document-type-field-routing-resolve.service';

const documentTypeFieldRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-type-field').then(m => m.DocumentTypeField),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-type-field-detail').then(m => m.DocumentTypeFieldDetail),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-type-field-update').then(m => m.DocumentTypeFieldUpdate),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-type-field-update').then(m => m.DocumentTypeFieldUpdate),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTypeFieldRoute;
