import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import FolderResolve from './route/folder-routing-resolve.service';

const folderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/folder').then(m => m.Folder),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/folder-detail').then(m => m.FolderDetail),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/folder-update').then(m => m.FolderUpdate),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/folder-update').then(m => m.FolderUpdate),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default folderRoute;
