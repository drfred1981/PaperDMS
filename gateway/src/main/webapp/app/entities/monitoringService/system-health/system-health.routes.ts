import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SystemHealthResolve from './route/system-health-routing-resolve.service';

const systemHealthRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/system-health.component').then(m => m.SystemHealthComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/system-health-detail.component').then(m => m.SystemHealthDetailComponent),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/system-health-update.component').then(m => m.SystemHealthUpdateComponent),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/system-health-update.component').then(m => m.SystemHealthUpdateComponent),
    resolve: {
      systemHealth: SystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default systemHealthRoute;
