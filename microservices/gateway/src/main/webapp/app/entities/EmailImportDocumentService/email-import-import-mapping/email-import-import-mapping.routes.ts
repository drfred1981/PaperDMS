import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmailImportImportMappingResolve from './route/email-import-import-mapping-routing-resolve.service';

const emailImportImportMappingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-import-import-mapping.component').then(m => m.EmailImportImportMappingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/email-import-import-mapping-detail.component').then(m => m.EmailImportImportMappingDetailComponent),
    resolve: {
      emailImportImportMapping: EmailImportImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/email-import-import-mapping-update.component').then(m => m.EmailImportImportMappingUpdateComponent),
    resolve: {
      emailImportImportMapping: EmailImportImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/email-import-import-mapping-update.component').then(m => m.EmailImportImportMappingUpdateComponent),
    resolve: {
      emailImportImportMapping: EmailImportImportMappingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailImportImportMappingRoute;
