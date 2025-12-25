import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentWatchResolve from './route/document-watch-routing-resolve.service';

const documentWatchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-watch.component').then(m => m.DocumentWatchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-watch-detail.component').then(m => m.DocumentWatchDetailComponent),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-watch-update.component').then(m => m.DocumentWatchUpdateComponent),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-watch-update.component').then(m => m.DocumentWatchUpdateComponent),
    resolve: {
      documentWatch: DocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentWatchRoute;
