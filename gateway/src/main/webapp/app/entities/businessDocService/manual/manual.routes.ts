import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ManualResolve from './route/manual-routing-resolve.service';

const manualRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/manual').then(m => m.Manual),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/manual-detail').then(m => m.ManualDetail),
    resolve: {
      manual: ManualResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/manual-update').then(m => m.ManualUpdate),
    resolve: {
      manual: ManualResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/manual-update').then(m => m.ManualUpdate),
    resolve: {
      manual: ManualResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default manualRoute;
