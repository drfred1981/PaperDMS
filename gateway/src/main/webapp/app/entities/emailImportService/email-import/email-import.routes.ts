import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import EmailImportResolve from './route/email-import-routing-resolve.service';

const emailImportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-import').then(m => m.EmailImport),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/email-import-detail').then(m => m.EmailImportDetail),
    resolve: {
      emailImport: EmailImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/email-import-update').then(m => m.EmailImportUpdate),
    resolve: {
      emailImport: EmailImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/email-import-update').then(m => m.EmailImportUpdate),
    resolve: {
      emailImport: EmailImportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailImportRoute;
