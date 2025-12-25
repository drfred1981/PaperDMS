import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImportMappingResolve from './route/import-mapping-routing-resolve.service';

const importMappingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/import-mapping.component').then(m => m.ImportMappingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/import-mapping-detail.component').then(m => m.ImportMappingDetailComponent),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/import-mapping-update.component').then(m => m.ImportMappingUpdateComponent),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/import-mapping-update.component').then(m => m.ImportMappingUpdateComponent),
    resolve: {
      importMapping: ImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default importMappingRoute;
