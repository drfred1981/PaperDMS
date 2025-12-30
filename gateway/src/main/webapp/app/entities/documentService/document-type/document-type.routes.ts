import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentTypeResolve from './route/document-type-routing-resolve.service';

const documentTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-type.component').then(m => m.DocumentTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-type-detail.component').then(m => m.DocumentTypeDetailComponent),
    resolve: {
      documentType: DocumentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-type-update.component').then(m => m.DocumentTypeUpdateComponent),
    resolve: {
      documentType: DocumentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-type-update.component').then(m => m.DocumentTypeUpdateComponent),
    resolve: {
      documentType: DocumentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTypeRoute;
