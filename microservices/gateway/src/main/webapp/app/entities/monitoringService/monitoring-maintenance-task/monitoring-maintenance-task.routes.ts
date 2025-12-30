import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringMaintenanceTaskResolve from './route/monitoring-maintenance-task-routing-resolve.service';

const monitoringMaintenanceTaskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-maintenance-task.component').then(m => m.MonitoringMaintenanceTaskComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/monitoring-maintenance-task-detail.component').then(m => m.MonitoringMaintenanceTaskDetailComponent),
    resolve: {
      monitoringMaintenanceTask: MonitoringMaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/monitoring-maintenance-task-update.component').then(m => m.MonitoringMaintenanceTaskUpdateComponent),
    resolve: {
      monitoringMaintenanceTask: MonitoringMaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/monitoring-maintenance-task-update.component').then(m => m.MonitoringMaintenanceTaskUpdateComponent),
    resolve: {
      monitoringMaintenanceTask: MonitoringMaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringMaintenanceTaskRoute;
