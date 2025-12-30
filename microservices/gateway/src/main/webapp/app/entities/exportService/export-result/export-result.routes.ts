import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExportResultResolve from './route/export-result-routing-resolve.service';

const exportResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-result.component').then(m => m.ExportResultComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-result-detail.component').then(m => m.ExportResultDetailComponent),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-result-update.component').then(m => m.ExportResultUpdateComponent),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-result-update.component').then(m => m.ExportResultUpdateComponent),
    resolve: {
      exportResult: ExportResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportResultRoute;
