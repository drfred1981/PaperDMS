import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExportJobResolve from './route/export-job-routing-resolve.service';

const exportJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-job').then(m => m.ExportJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-job-detail').then(m => m.ExportJobDetail),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-job-update').then(m => m.ExportJobUpdate),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-job-update').then(m => m.ExportJobUpdate),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportJobRoute;
