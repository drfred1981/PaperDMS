import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringDocumentWatchResolve from './route/monitoring-document-watch-routing-resolve.service';

const monitoringDocumentWatchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-document-watch.component').then(m => m.MonitoringDocumentWatchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitoring-document-watch-detail.component').then(m => m.MonitoringDocumentWatchDetailComponent),
    resolve: {
      monitoringDocumentWatch: MonitoringDocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitoring-document-watch-update.component').then(m => m.MonitoringDocumentWatchUpdateComponent),
    resolve: {
      monitoringDocumentWatch: MonitoringDocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitoring-document-watch-update.component').then(m => m.MonitoringDocumentWatchUpdateComponent),
    resolve: {
      monitoringDocumentWatch: MonitoringDocumentWatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringDocumentWatchRoute;
