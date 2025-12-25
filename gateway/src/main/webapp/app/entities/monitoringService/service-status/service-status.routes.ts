import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServiceStatusResolve from './route/service-status-routing-resolve.service';

const serviceStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-status.component').then(m => m.ServiceStatusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-status-detail.component').then(m => m.ServiceStatusDetailComponent),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-status-update.component').then(m => m.ServiceStatusUpdateComponent),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-status-update.component').then(m => m.ServiceStatusUpdateComponent),
    resolve: {
      serviceStatus: ServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceStatusRoute;
