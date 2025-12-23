import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentMetadataResolve from './route/document-metadata-routing-resolve.service';

const documentMetadataRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-metadata').then(m => m.DocumentMetadata),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-metadata-detail').then(m => m.DocumentMetadataDetail),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-metadata-update').then(m => m.DocumentMetadataUpdate),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-metadata-update').then(m => m.DocumentMetadataUpdate),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentMetadataRoute;
