import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentRelationResolve from './route/document-relation-routing-resolve.service';

const documentRelationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-relation').then(m => m.DocumentRelation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-relation-detail').then(m => m.DocumentRelationDetail),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-relation-update').then(m => m.DocumentRelationUpdate),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-relation-update').then(m => m.DocumentRelationUpdate),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentRelationRoute;
