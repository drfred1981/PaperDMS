import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentProcessResolve from './route/document-process-routing-resolve.service';

const documentProcessRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-process').then(m => m.DocumentProcess),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-process-detail').then(m => m.DocumentProcessDetail),
    resolve: {
      documentProcess: DocumentProcessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-process-update').then(m => m.DocumentProcessUpdate),
    resolve: {
      documentProcess: DocumentProcessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-process-update').then(m => m.DocumentProcessUpdate),
    resolve: {
      documentProcess: DocumentProcessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentProcessRoute;
