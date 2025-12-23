import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PermissionGroupResolve from './route/permission-group-routing-resolve.service';

const permissionGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/permission-group').then(m => m.PermissionGroup),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/permission-group-detail').then(m => m.PermissionGroupDetail),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/permission-group-update').then(m => m.PermissionGroupUpdate),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/permission-group-update').then(m => m.PermissionGroupUpdate),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default permissionGroupRoute;
