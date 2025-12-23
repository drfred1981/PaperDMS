import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import NotificationPreferenceResolve from './route/notification-preference-routing-resolve.service';

const notificationPreferenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-preference').then(m => m.NotificationPreference),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-preference-detail').then(m => m.NotificationPreferenceDetail),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-preference-update').then(m => m.NotificationPreferenceUpdate),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-preference-update').then(m => m.NotificationPreferenceUpdate),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationPreferenceRoute;
