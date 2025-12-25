import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WebhookLogResolve from './route/webhook-log-routing-resolve.service';

const webhookLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/webhook-log.component').then(m => m.WebhookLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/webhook-log-detail.component').then(m => m.WebhookLogDetailComponent),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/webhook-log-update.component').then(m => m.WebhookLogUpdateComponent),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/webhook-log-update.component').then(m => m.WebhookLogUpdateComponent),
    resolve: {
      webhookLog: WebhookLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default webhookLogRoute;
