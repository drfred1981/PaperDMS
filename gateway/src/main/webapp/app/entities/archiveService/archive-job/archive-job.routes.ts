import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ArchiveJobResolve from './route/archive-job-routing-resolve.service';

const archiveJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/archive-job').then(m => m.ArchiveJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/archive-job-detail').then(m => m.ArchiveJobDetail),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/archive-job-update').then(m => m.ArchiveJobUpdate),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/archive-job-update').then(m => m.ArchiveJobUpdate),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default archiveJobRoute;
