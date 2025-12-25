import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SearchFacetResolve from './route/search-facet-routing-resolve.service';

const searchFacetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-facet.component').then(m => m.SearchFacetComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-facet-detail.component').then(m => m.SearchFacetDetailComponent),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-facet-update.component').then(m => m.SearchFacetUpdateComponent),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-facet-update.component').then(m => m.SearchFacetUpdateComponent),
    resolve: {
      searchFacet: SearchFacetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchFacetRoute;
