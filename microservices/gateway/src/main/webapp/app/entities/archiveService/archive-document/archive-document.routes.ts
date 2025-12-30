import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ArchiveDocumentResolve from './route/archive-document-routing-resolve.service';

const archiveDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/archive-document.component').then(m => m.ArchiveDocumentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/archive-document-detail.component').then(m => m.ArchiveDocumentDetailComponent),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/archive-document-update.component').then(m => m.ArchiveDocumentUpdateComponent),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/archive-document-update.component').then(m => m.ArchiveDocumentUpdateComponent),
    resolve: {
      archiveDocument: ArchiveDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default archiveDocumentRoute;
