import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentCommentResolve from './route/document-comment-routing-resolve.service';

const documentCommentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-comment').then(m => m.DocumentComment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-comment-detail').then(m => m.DocumentCommentDetail),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-comment-update').then(m => m.DocumentCommentUpdate),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-comment-update').then(m => m.DocumentCommentUpdate),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentCommentRoute;
