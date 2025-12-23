import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentStatisticsResolve from './route/document-statistics-routing-resolve.service';

const documentStatisticsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-statistics').then(m => m.DocumentStatistics),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-statistics-detail').then(m => m.DocumentStatisticsDetail),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-statistics-update').then(m => m.DocumentStatisticsUpdate),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-statistics-update').then(m => m.DocumentStatisticsUpdate),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentStatisticsRoute;
