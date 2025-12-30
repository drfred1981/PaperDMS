import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringAlertResolve from './route/monitoring-alert-routing-resolve.service';

const monitoringAlertRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-alert.component').then(m => m.MonitoringAlertComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitoring-alert-detail.component').then(m => m.MonitoringAlertDetailComponent),
    resolve: {
      monitoringAlert: MonitoringAlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitoring-alert-update.component').then(m => m.MonitoringAlertUpdateComponent),
    resolve: {
      monitoringAlert: MonitoringAlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitoring-alert-update.component').then(m => m.MonitoringAlertUpdateComponent),
    resolve: {
      monitoringAlert: MonitoringAlertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringAlertRoute;
