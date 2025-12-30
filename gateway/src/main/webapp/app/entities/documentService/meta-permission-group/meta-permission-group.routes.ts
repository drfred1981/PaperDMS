import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaPermissionGroupResolve from './route/meta-permission-group-routing-resolve.service';

const metaPermissionGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-permission-group.component').then(m => m.MetaPermissionGroupComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-permission-group-detail.component').then(m => m.MetaPermissionGroupDetailComponent),
    resolve: {
      metaPermissionGroup: MetaPermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-permission-group-update.component').then(m => m.MetaPermissionGroupUpdateComponent),
    resolve: {
      metaPermissionGroup: MetaPermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-permission-group-update.component').then(m => m.MetaPermissionGroupUpdateComponent),
    resolve: {
      metaPermissionGroup: MetaPermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaPermissionGroupRoute;
