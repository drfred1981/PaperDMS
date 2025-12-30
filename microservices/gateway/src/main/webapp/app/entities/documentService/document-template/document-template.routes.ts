import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentTemplateResolve from './route/document-template-routing-resolve.service';

const documentTemplateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-template.component').then(m => m.DocumentTemplateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-template-detail.component').then(m => m.DocumentTemplateDetailComponent),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-template-update.component').then(m => m.DocumentTemplateUpdateComponent),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-template-update.component').then(m => m.DocumentTemplateUpdateComponent),
    resolve: {
      documentTemplate: DocumentTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTemplateRoute;
