import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FolderResolve from './route/folder-routing-resolve.service';

const folderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/folder.component').then(m => m.FolderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/folder-detail.component').then(m => m.FolderDetailComponent),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/folder-update.component').then(m => m.FolderUpdateComponent),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/folder-update.component').then(m => m.FolderUpdateComponent),
    resolve: {
      folder: FolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default folderRoute;
