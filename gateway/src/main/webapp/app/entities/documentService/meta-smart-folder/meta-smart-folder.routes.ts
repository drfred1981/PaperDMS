import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaSmartFolderResolve from './route/meta-smart-folder-routing-resolve.service';

const metaSmartFolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-smart-folder.component').then(m => m.MetaSmartFolderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-smart-folder-detail.component').then(m => m.MetaSmartFolderDetailComponent),
    resolve: {
      metaSmartFolder: MetaSmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-smart-folder-update.component').then(m => m.MetaSmartFolderUpdateComponent),
    resolve: {
      metaSmartFolder: MetaSmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-smart-folder-update.component').then(m => m.MetaSmartFolderUpdateComponent),
    resolve: {
      metaSmartFolder: MetaSmartFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaSmartFolderRoute;
