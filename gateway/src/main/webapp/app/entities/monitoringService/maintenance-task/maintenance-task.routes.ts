import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import MaintenanceTaskResolve from './route/maintenance-task-routing-resolve.service';

const maintenanceTaskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maintenance-task').then(m => m.MaintenanceTask),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maintenance-task-detail').then(m => m.MaintenanceTaskDetail),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maintenance-task-update').then(m => m.MaintenanceTaskUpdate),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maintenance-task-update').then(m => m.MaintenanceTaskUpdate),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maintenanceTaskRoute;
