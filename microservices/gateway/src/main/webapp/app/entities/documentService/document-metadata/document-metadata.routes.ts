import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentMetadataResolve from './route/document-metadata-routing-resolve.service';

const documentMetadataRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-metadata.component').then(m => m.DocumentMetadataComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-metadata-detail.component').then(m => m.DocumentMetadataDetailComponent),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-metadata-update.component').then(m => m.DocumentMetadataUpdateComponent),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-metadata-update.component').then(m => m.DocumentMetadataUpdateComponent),
    resolve: {
      documentMetadata: DocumentMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentMetadataRoute;
