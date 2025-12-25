import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ScannedPageResolve from './route/scanned-page-routing-resolve.service';

const scannedPageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scanned-page.component').then(m => m.ScannedPageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scanned-page-detail.component').then(m => m.ScannedPageDetailComponent),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scanned-page-update.component').then(m => m.ScannedPageUpdateComponent),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scanned-page-update.component').then(m => m.ScannedPageUpdateComponent),
    resolve: {
      scannedPage: ScannedPageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scannedPageRoute;
