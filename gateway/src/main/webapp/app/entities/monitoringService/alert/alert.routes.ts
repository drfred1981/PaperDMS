import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import AlertResolve from './route/alert-routing-resolve.service';

const alertRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alert').then(m => m.Alert),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alert-detail').then(m => m.AlertDetail),
    resolve: {
      alert: AlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alert-update').then(m => m.AlertUpdate),
    resolve: {
      alert: AlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alert-update').then(m => m.AlertUpdate),
    resolve: {
      alert: AlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alertRoute;
