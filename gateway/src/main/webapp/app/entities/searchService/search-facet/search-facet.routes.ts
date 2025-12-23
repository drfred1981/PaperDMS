import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SearchFacetResolve from './route/search-facet-routing-resolve.service';

const searchFacetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-facet').then(m => m.SearchFacet),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-facet-detail').then(m => m.SearchFacetDetail),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-facet-update').then(m => m.SearchFacetUpdate),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-facet-update').then(m => m.SearchFacetUpdate),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchFacetRoute;
