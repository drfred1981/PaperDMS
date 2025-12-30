import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentPermissionResolve from './route/document-permission-routing-resolve.service';

const documentPermissionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-permission.component').then(m => m.DocumentPermissionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-permission-detail.component').then(m => m.DocumentPermissionDetailComponent),
    resolve: {
      documentPermission: DocumentPermissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-permission-update.component').then(m => m.DocumentPermissionUpdateComponent),
    resolve: {
      documentPermission: DocumentPermissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-permission-update.component').then(m => m.DocumentPermissionUpdateComponent),
    resolve: {
      documentPermission: DocumentPermissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentPermissionRoute;
