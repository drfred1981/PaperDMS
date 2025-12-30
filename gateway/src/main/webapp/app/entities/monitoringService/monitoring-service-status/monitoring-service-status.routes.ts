import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringServiceStatusResolve from './route/monitoring-service-status-routing-resolve.service';

const monitoringServiceStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-service-status.component').then(m => m.MonitoringServiceStatusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitoring-service-status-detail.component').then(m => m.MonitoringServiceStatusDetailComponent),
    resolve: {
      monitoringServiceStatus: MonitoringServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitoring-service-status-update.component').then(m => m.MonitoringServiceStatusUpdateComponent),
    resolve: {
      monitoringServiceStatus: MonitoringServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitoring-service-status-update.component').then(m => m.MonitoringServiceStatusUpdateComponent),
    resolve: {
      monitoringServiceStatus: MonitoringServiceStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringServiceStatusRoute;
