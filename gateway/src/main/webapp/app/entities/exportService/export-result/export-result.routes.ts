import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExportResultResolve from './route/export-result-routing-resolve.service';

const exportResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-result').then(m => m.ExportResult),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-result-detail').then(m => m.ExportResultDetail),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-result-update').then(m => m.ExportResultUpdate),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-result-update').then(m => m.ExportResultUpdate),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportResultRoute;
