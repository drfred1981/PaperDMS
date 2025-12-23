import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentTemplateResolve from './route/document-template-routing-resolve.service';

const documentTemplateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-template').then(m => m.DocumentTemplate),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-template-detail').then(m => m.DocumentTemplateDetail),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-template-update').then(m => m.DocumentTemplateUpdate),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-template-update').then(m => m.DocumentTemplateUpdate),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTemplateRoute;
