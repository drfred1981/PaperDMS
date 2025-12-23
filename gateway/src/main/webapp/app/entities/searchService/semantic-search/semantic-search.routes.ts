import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SemanticSearchResolve from './route/semantic-search-routing-resolve.service';

const semanticSearchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/semantic-search').then(m => m.SemanticSearch),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/semantic-search-detail').then(m => m.SemanticSearchDetail),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/semantic-search-update').then(m => m.SemanticSearchUpdate),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/semantic-search-update').then(m => m.SemanticSearchUpdate),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default semanticSearchRoute;
