import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentServiceStatusResolve from './route/document-service-status-routing-resolve.service';

const documentServiceStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-service-status.component').then(m => m.DocumentServiceStatusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-service-status-detail.component').then(m => m.DocumentServiceStatusDetailComponent),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-service-status-update.component').then(m => m.DocumentServiceStatusUpdateComponent),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-service-status-update.component').then(m => m.DocumentServiceStatusUpdateComponent),
    resolve: {
      documentServiceStatus: DocumentServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentServiceStatusRoute;
