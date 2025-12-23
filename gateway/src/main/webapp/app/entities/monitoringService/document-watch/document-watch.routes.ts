import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentWatchResolve from './route/document-watch-routing-resolve.service';

const documentWatchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-watch').then(m => m.DocumentWatch),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-watch-detail').then(m => m.DocumentWatchDetail),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-watch-update').then(m => m.DocumentWatchUpdate),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-watch-update').then(m => m.DocumentWatchUpdate),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentWatchRoute;
