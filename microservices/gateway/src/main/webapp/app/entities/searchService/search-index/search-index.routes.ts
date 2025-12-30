import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SearchIndexResolve from './route/search-index-routing-resolve.service';

const searchIndexRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-index.component').then(m => m.SearchIndexComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-index-detail.component').then(m => m.SearchIndexDetailComponent),
    resolve: {
      searchIndex: SearchIndexResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-index-update.component').then(m => m.SearchIndexUpdateComponent),
    resolve: {
      searchIndex: SearchIndexResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-index-update.component').then(m => m.SearchIndexUpdateComponent),
    resolve: {
      searchIndex: SearchIndexResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchIndexRoute;
