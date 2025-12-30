import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SearchQueryResolve from './route/search-query-routing-resolve.service';

const searchQueryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-query.component').then(m => m.SearchQueryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-query-detail.component').then(m => m.SearchQueryDetailComponent),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-query-update.component').then(m => m.SearchQueryUpdateComponent),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-query-update.component').then(m => m.SearchQueryUpdateComponent),
    resolve: {
      searchQuery: SearchQueryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchQueryRoute;
