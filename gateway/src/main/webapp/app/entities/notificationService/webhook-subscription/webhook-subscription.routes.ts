import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WebhookSubscriptionResolve from './route/webhook-subscription-routing-resolve.service';

const webhookSubscriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/webhook-subscription').then(m => m.WebhookSubscription),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/webhook-subscription-detail').then(m => m.WebhookSubscriptionDetail),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/webhook-subscription-update').then(m => m.WebhookSubscriptionUpdate),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/webhook-subscription-update').then(m => m.WebhookSubscriptionUpdate),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default webhookSubscriptionRoute;
