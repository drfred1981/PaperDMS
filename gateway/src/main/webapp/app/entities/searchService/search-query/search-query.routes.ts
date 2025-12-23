import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SearchQueryResolve from './route/search-query-routing-resolve.service';

const searchQueryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-query').then(m => m.SearchQuery),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-query-detail').then(m => m.SearchQueryDetail),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-query-update').then(m => m.SearchQueryUpdate),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-query-update').then(m => m.SearchQueryUpdate),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchQueryRoute;
