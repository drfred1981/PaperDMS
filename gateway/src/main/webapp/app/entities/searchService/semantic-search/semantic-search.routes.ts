import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SemanticSearchResolve from './route/semantic-search-routing-resolve.service';

const semanticSearchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/semantic-search.component').then(m => m.SemanticSearchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/semantic-search-detail.component').then(m => m.SemanticSearchDetailComponent),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/semantic-search-update.component').then(m => m.SemanticSearchUpdateComponent),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/semantic-search-update.component').then(m => m.SemanticSearchUpdateComponent),
    resolve: {
      semanticSearch: SemanticSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default semanticSearchRoute;
