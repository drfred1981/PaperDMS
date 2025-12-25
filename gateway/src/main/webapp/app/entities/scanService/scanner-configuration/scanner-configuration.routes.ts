import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ScannerConfigurationResolve from './route/scanner-configuration-routing-resolve.service';

const scannerConfigurationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scanner-configuration.component').then(m => m.ScannerConfigurationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scanner-configuration-detail.component').then(m => m.ScannerConfigurationDetailComponent),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scanner-configuration-update.component').then(m => m.ScannerConfigurationUpdateComponent),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scanner-configuration-update.component').then(m => m.ScannerConfigurationUpdateComponent),
    resolve: {
      scannerConfiguration: ScannerConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scannerConfigurationRoute;
