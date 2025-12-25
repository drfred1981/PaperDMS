import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ArchiveJobResolve from './route/archive-job-routing-resolve.service';

const archiveJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/archive-job.component').then(m => m.ArchiveJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/archive-job-detail.component').then(m => m.ArchiveJobDetailComponent),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/archive-job-update.component').then(m => m.ArchiveJobUpdateComponent),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/archive-job-update.component').then(m => m.ArchiveJobUpdateComponent),
    resolve: {
      archiveJob: ArchiveJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default archiveJobRoute;
