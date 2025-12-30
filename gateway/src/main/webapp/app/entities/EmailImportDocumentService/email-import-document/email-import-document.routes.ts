import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmailImportDocumentResolve from './route/email-import-document-routing-resolve.service';

const emailImportDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-import-document.component').then(m => m.EmailImportDocumentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/email-import-document-detail.component').then(m => m.EmailImportDocumentDetailComponent),
    resolve: {
      emailImportDocument: EmailImportDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/email-import-document-update.component').then(m => m.EmailImportDocumentUpdateComponent),
    resolve: {
      emailImportDocument: EmailImportDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/email-import-document-update.component').then(m => m.EmailImportDocumentUpdateComponent),
    resolve: {
      emailImportDocument: EmailImportDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailImportDocumentRoute;
