import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentRelationResolve from './route/document-relation-routing-resolve.service';

const documentRelationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-relation.component').then(m => m.DocumentRelationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-relation-detail.component').then(m => m.DocumentRelationDetailComponent),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-relation-update.component').then(m => m.DocumentRelationUpdateComponent),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-relation-update.component').then(m => m.DocumentRelationUpdateComponent),
    resolve: {
      documentRelation: DocumentRelationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentRelationRoute;
