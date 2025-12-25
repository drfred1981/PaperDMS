import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SmartFolderResolve from './route/smart-folder-routing-resolve.service';

const smartFolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/smart-folder.component').then(m => m.SmartFolderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/smart-folder-detail.component').then(m => m.SmartFolderDetailComponent),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/smart-folder-update.component').then(m => m.SmartFolderUpdateComponent),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/smart-folder-update.component').then(m => m.SmartFolderUpdateComponent),
    resolve: {
      smartFolder: SmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default smartFolderRoute;
