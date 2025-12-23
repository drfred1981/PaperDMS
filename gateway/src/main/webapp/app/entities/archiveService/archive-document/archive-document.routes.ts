import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ArchiveDocumentResolve from './route/archive-document-routing-resolve.service';

const archiveDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/archive-document').then(m => m.ArchiveDocument),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/archive-document-detail').then(m => m.ArchiveDocumentDetail),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/archive-document-update').then(m => m.ArchiveDocumentUpdate),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/archive-document-update').then(m => m.ArchiveDocumentUpdate),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default archiveDocumentRoute;
