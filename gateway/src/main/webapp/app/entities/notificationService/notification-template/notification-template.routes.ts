import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import NotificationTemplateResolve from './route/notification-template-routing-resolve.service';

const notificationTemplateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-template').then(m => m.NotificationTemplate),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-template-detail').then(m => m.NotificationTemplateDetail),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-template-update').then(m => m.NotificationTemplateUpdate),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-template-update').then(m => m.NotificationTemplateUpdate),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationTemplateRoute;
