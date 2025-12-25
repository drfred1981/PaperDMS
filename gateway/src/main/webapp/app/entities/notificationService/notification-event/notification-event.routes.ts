import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import NotificationEventResolve from './route/notification-event-routing-resolve.service';

const notificationEventRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-event.component').then(m => m.NotificationEventComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-event-detail.component').then(m => m.NotificationEventDetailComponent),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-event-update.component').then(m => m.NotificationEventUpdateComponent),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-event-update.component').then(m => m.NotificationEventUpdateComponent),
    resolve: {
      notificationEvent: NotificationEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationEventRoute;
