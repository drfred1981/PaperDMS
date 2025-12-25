import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentAuditResolve from './route/document-audit-routing-resolve.service';

const documentAuditRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-audit.component').then(m => m.DocumentAuditComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-audit-detail.component').then(m => m.DocumentAuditDetailComponent),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-audit-update.component').then(m => m.DocumentAuditUpdateComponent),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-audit-update.component').then(m => m.DocumentAuditUpdateComponent),
    resolve: {
      documentAudit: DocumentAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentAuditRoute;
