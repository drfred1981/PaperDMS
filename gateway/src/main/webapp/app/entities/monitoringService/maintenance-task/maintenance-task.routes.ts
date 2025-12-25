import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaintenanceTaskResolve from './route/maintenance-task-routing-resolve.service';

const maintenanceTaskRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maintenance-task.component').then(m => m.MaintenanceTaskComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maintenance-task-detail.component').then(m => m.MaintenanceTaskDetailComponent),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maintenance-task-update.component').then(m => m.MaintenanceTaskUpdateComponent),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maintenance-task-update.component').then(m => m.MaintenanceTaskUpdateComponent),
    resolve: {
      maintenanceTask: MaintenanceTaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maintenanceTaskRoute;
