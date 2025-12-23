import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExportPatternResolve from './route/export-pattern-routing-resolve.service';

const exportPatternRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-pattern').then(m => m.ExportPattern),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-pattern-detail').then(m => m.ExportPatternDetail),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-pattern-update').then(m => m.ExportPatternUpdate),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-pattern-update').then(m => m.ExportPatternUpdate),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportPatternRoute;
