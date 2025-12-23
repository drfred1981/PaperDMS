import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentAuditResolve from './route/document-audit-routing-resolve.service';

const documentAuditRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-audit').then(m => m.DocumentAudit),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-audit-detail').then(m => m.DocumentAuditDetail),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-audit-update').then(m => m.DocumentAuditUpdate),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-audit-update').then(m => m.DocumentAuditUpdate),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentAuditRoute;
