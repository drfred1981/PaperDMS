import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExportPatternResolve from './route/export-pattern-routing-resolve.service';

const exportPatternRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-pattern.component').then(m => m.ExportPatternComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-pattern-detail.component').then(m => m.ExportPatternDetailComponent),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-pattern-update.component').then(m => m.ExportPatternUpdateComponent),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-pattern-update.component').then(m => m.ExportPatternUpdateComponent),
    resolve: {
      exportPattern: ExportPatternResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportPatternRoute;
