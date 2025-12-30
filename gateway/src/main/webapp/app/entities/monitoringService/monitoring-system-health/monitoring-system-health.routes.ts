import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringSystemHealthResolve from './route/monitoring-system-health-routing-resolve.service';

const monitoringSystemHealthRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-system-health.component').then(m => m.MonitoringSystemHealthComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitoring-system-health-detail.component').then(m => m.MonitoringSystemHealthDetailComponent),
    resolve: {
      monitoringSystemHealth: MonitoringSystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitoring-system-health-update.component').then(m => m.MonitoringSystemHealthUpdateComponent),
    resolve: {
      monitoringSystemHealth: MonitoringSystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitoring-system-health-update.component').then(m => m.MonitoringSystemHealthUpdateComponent),
    resolve: {
      monitoringSystemHealth: MonitoringSystemHealthResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringSystemHealthRoute;
