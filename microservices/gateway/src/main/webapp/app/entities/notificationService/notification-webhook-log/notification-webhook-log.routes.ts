import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import NotificationWebhookLogResolve from './route/notification-webhook-log-routing-resolve.service';

const notificationWebhookLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-webhook-log.component').then(m => m.NotificationWebhookLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-webhook-log-detail.component').then(m => m.NotificationWebhookLogDetailComponent),
    resolve: {
      notificationWebhookLog: NotificationWebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-webhook-log-update.component').then(m => m.NotificationWebhookLogUpdateComponent),
    resolve: {
      notificationWebhookLog: NotificationWebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-webhook-log-update.component').then(m => m.NotificationWebhookLogUpdateComponent),
    resolve: {
      notificationWebhookLog: NotificationWebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationWebhookLogRoute;
