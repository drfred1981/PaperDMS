import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentTypeFieldResolve from './route/document-type-field-routing-resolve.service';

const documentTypeFieldRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-type-field.component').then(m => m.DocumentTypeFieldComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-type-field-detail.component').then(m => m.DocumentTypeFieldDetailComponent),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-type-field-update.component').then(m => m.DocumentTypeFieldUpdateComponent),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-type-field-update.component').then(m => m.DocumentTypeFieldUpdateComponent),
    resolve: {
      documentTypeField: DocumentTypeFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTypeFieldRoute;
