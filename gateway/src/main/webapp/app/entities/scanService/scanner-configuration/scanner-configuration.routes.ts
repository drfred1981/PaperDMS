import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScannerConfigurationResolve from './route/scanner-configuration-routing-resolve.service';

const scannerConfigurationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scanner-configuration').then(m => m.ScannerConfiguration),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scanner-configuration-detail').then(m => m.ScannerConfigurationDetail),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scanner-configuration-update').then(m => m.ScannerConfigurationUpdate),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scanner-configuration-update').then(m => m.ScannerConfigurationUpdate),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scannerConfigurationRoute;
