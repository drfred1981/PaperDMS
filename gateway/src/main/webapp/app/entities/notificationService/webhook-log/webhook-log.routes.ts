import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import WebhookLogResolve from './route/webhook-log-routing-resolve.service';

const webhookLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/webhook-log').then(m => m.WebhookLog),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/webhook-log-detail').then(m => m.WebhookLogDetail),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/webhook-log-update').then(m => m.WebhookLogUpdate),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/webhook-log-update').then(m => m.WebhookLogUpdate),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default webhookLogRoute;
