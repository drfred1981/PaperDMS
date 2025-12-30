import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import NotificationWebhookSubscriptionResolve from './route/notification-webhook-subscription-routing-resolve.service';

const notificationWebhookSubscriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-webhook-subscription.component').then(m => m.NotificationWebhookSubscriptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/notification-webhook-subscription-detail.component').then(m => m.NotificationWebhookSubscriptionDetailComponent),
    resolve: {
      notificationWebhookSubscription: NotificationWebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/notification-webhook-subscription-update.component').then(m => m.NotificationWebhookSubscriptionUpdateComponent),
    resolve: {
      notificationWebhookSubscription: NotificationWebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/notification-webhook-subscription-update.component').then(m => m.NotificationWebhookSubscriptionUpdateComponent),
    resolve: {
      notificationWebhookSubscription: NotificationWebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationWebhookSubscriptionRoute;
