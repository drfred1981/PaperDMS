import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WebhookSubscriptionResolve from './route/webhook-subscription-routing-resolve.service';

const webhookSubscriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/webhook-subscription.component').then(m => m.WebhookSubscriptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/webhook-subscription-detail.component').then(m => m.WebhookSubscriptionDetailComponent),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/webhook-subscription-update.component').then(m => m.WebhookSubscriptionUpdateComponent),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/webhook-subscription-update.component').then(m => m.WebhookSubscriptionUpdateComponent),
    resolve: {
      webhookSubscription: WebhookSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default webhookSubscriptionRoute;
