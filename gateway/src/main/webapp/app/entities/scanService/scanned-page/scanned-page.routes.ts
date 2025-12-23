import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScannedPageResolve from './route/scanned-page-routing-resolve.service';

const scannedPageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scanned-page').then(m => m.ScannedPage),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scanned-page-detail').then(m => m.ScannedPageDetail),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scanned-page-update').then(m => m.ScannedPageUpdate),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scanned-page-update').then(m => m.ScannedPageUpdate),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scannedPageRoute;
