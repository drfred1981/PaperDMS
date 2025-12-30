import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import NotificationTemplateResolve from './route/notification-template-routing-resolve.service';

const notificationTemplateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-template.component').then(m => m.NotificationTemplateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-template-detail.component').then(m => m.NotificationTemplateDetailComponent),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-template-update.component').then(m => m.NotificationTemplateUpdateComponent),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-template-update.component').then(m => m.NotificationTemplateUpdateComponent),
    resolve: {
      notificationTemplate: NotificationTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationTemplateRoute;
