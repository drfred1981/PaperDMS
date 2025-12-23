import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentServiceStatusResolve from './route/document-service-status-routing-resolve.service';

const documentServiceStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-service-status').then(m => m.DocumentServiceStatus),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-service-status-detail').then(m => m.DocumentServiceStatusDetail),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-service-status-update').then(m => m.DocumentServiceStatusUpdate),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-service-status-update').then(m => m.DocumentServiceStatusUpdate),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentServiceStatusRoute;
