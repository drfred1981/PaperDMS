import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentStatisticsResolve from './route/document-statistics-routing-resolve.service';

const documentStatisticsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-statistics.component').then(m => m.DocumentStatisticsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-statistics-detail.component').then(m => m.DocumentStatisticsDetailComponent),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-statistics-update.component').then(m => m.DocumentStatisticsUpdateComponent),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-statistics-update.component').then(m => m.DocumentStatisticsUpdateComponent),
    resolve: {
      documentStatistics: DocumentStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentStatisticsRoute;
