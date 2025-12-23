import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ImportMappingResolve from './route/import-mapping-routing-resolve.service';

const importMappingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/import-mapping').then(m => m.ImportMapping),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/import-mapping-detail').then(m => m.ImportMappingDetail),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/import-mapping-update').then(m => m.ImportMappingUpdate),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/import-mapping-update').then(m => m.ImportMappingUpdate),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default importMappingRoute;
