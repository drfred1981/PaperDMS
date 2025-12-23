import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import NotificationEventResolve from './route/notification-event-routing-resolve.service';

const notificationEventRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-event').then(m => m.NotificationEvent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-event-detail').then(m => m.NotificationEventDetail),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-event-update').then(m => m.NotificationEventUpdate),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-event-update').then(m => m.NotificationEventUpdate),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationEventRoute;
