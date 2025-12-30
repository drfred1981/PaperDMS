import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaFolderResolve from './route/meta-folder-routing-resolve.service';

const metaFolderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-folder.component').then(m => m.MetaFolderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-folder-detail.component').then(m => m.MetaFolderDetailComponent),
    resolve: {
      metaFolder: MetaFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-folder-update.component').then(m => m.MetaFolderUpdateComponent),
    resolve: {
      metaFolder: MetaFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-folder-update.component').then(m => m.MetaFolderUpdateComponent),
    resolve: {
      metaFolder: MetaFolderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaFolderRoute;
