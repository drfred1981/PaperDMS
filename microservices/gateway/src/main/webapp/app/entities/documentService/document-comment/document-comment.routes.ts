import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentCommentResolve from './route/document-comment-routing-resolve.service';

const documentCommentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-comment.component').then(m => m.DocumentCommentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-comment-detail.component').then(m => m.DocumentCommentDetailComponent),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-comment-update.component').then(m => m.DocumentCommentUpdateComponent),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-comment-update.component').then(m => m.DocumentCommentUpdateComponent),
    resolve: {
      documentComment: DocumentCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentCommentRoute;
