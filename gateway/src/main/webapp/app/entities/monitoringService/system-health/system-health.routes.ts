import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SystemHealthResolve from './route/system-health-routing-resolve.service';

const systemHealthRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-health').then(m => m.SystemHealth),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-health-detail').then(m => m.SystemHealthDetail),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-health-update').then(m => m.SystemHealthUpdate),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-health-update').then(m => m.SystemHealthUpdate),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default systemHealthRoute;
