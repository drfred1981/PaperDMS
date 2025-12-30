import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentExtractedFieldResolve from './route/document-extracted-field-routing-resolve.service';

const documentExtractedFieldRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-extracted-field.component').then(m => m.DocumentExtractedFieldComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-extracted-field-detail.component').then(m => m.DocumentExtractedFieldDetailComponent),
    resolve: {
      documentExtractedField: DocumentExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-extracted-field-update.component').then(m => m.DocumentExtractedFieldUpdateComponent),
    resolve: {
      documentExtractedField: DocumentExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-extracted-field-update.component').then(m => m.DocumentExtractedFieldUpdateComponent),
    resolve: {
      documentExtractedField: DocumentExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentExtractedFieldRoute;
