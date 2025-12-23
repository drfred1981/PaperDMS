import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SavedSearchResolve from './route/saved-search-routing-resolve.service';

const savedSearchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/saved-search').then(m => m.SavedSearch),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/saved-search-detail').then(m => m.SavedSearchDetail),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/saved-search-update').then(m => m.SavedSearchUpdate),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/saved-search-update').then(m => m.SavedSearchUpdate),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default savedSearchRoute;
