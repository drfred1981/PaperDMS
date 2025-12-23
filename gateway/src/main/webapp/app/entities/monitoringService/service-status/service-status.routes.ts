import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ServiceStatusResolve from './route/service-status-routing-resolve.service';

const serviceStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-status').then(m => m.ServiceStatus),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-status-detail').then(m => m.ServiceStatusDetail),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-status-update').then(m => m.ServiceStatusUpdate),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-status-update').then(m => m.ServiceStatusUpdate),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceStatusRoute;
