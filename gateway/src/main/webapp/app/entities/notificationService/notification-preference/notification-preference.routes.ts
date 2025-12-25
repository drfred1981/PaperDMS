import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import NotificationPreferenceResolve from './route/notification-preference-routing-resolve.service';

const notificationPreferenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/notification-preference.component').then(m => m.NotificationPreferenceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/notification-preference-detail.component').then(m => m.NotificationPreferenceDetailComponent),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/notification-preference-update.component').then(m => m.NotificationPreferenceUpdateComponent),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/notification-preference-update.component').then(m => m.NotificationPreferenceUpdateComponent),
    resolve: {
      notificationPreference: NotificationPreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationPreferenceRoute;
