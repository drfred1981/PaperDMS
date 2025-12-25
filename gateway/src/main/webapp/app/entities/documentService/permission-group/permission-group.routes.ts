import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PermissionGroupResolve from './route/permission-group-routing-resolve.service';

const permissionGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/permission-group.component').then(m => m.PermissionGroupComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/permission-group-detail.component').then(m => m.PermissionGroupDetailComponent),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/permission-group-update.component').then(m => m.PermissionGroupUpdateComponent),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/permission-group-update.component').then(m => m.PermissionGroupUpdateComponent),
    resolve: {
      permissionGroup: PermissionGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default permissionGroupRoute;
