import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExportJobResolve from './route/export-job-routing-resolve.service';

const exportJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/export-job.component').then(m => m.ExportJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/export-job-detail.component').then(m => m.ExportJobDetailComponent),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/export-job-update.component').then(m => m.ExportJobUpdateComponent),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/export-job-update.component').then(m => m.ExportJobUpdateComponent),
    resolve: {
      exportJob: ExportJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default exportJobRoute;
