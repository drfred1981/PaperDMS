import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentTagResolve from './route/document-tag-routing-resolve.service';

const documentTagRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-tag.component').then(m => m.DocumentTagComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-tag-detail.component').then(m => m.DocumentTagDetailComponent),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-tag-update.component').then(m => m.DocumentTagUpdateComponent),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-tag-update.component').then(m => m.DocumentTagUpdateComponent),
    resolve: {
      documentTag: DocumentTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentTagRoute;
