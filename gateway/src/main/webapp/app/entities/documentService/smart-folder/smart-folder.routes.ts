import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SmartFolderResolve from './route/smart-folder-routing-resolve.service';

const smartFolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/smart-folder').then(m => m.SmartFolder),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/smart-folder-detail').then(m => m.SmartFolderDetail),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/smart-folder-update').then(m => m.SmartFolderUpdate),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/smart-folder-update').then(m => m.SmartFolderUpdate),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default smartFolderRoute;
