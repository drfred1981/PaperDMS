import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SavedSearchResolve from './route/saved-search-routing-resolve.service';

const savedSearchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/saved-search.component').then(m => m.SavedSearchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/saved-search-detail.component').then(m => m.SavedSearchDetailComponent),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/saved-search-update.component').then(m => m.SavedSearchUpdateComponent),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/saved-search-update.component').then(m => m.SavedSearchUpdateComponent),
    resolve: {
      savedSearch: SavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default savedSearchRoute;
