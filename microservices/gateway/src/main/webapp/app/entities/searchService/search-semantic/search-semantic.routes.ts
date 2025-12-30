import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SearchSemanticResolve from './route/search-semantic-routing-resolve.service';

const searchSemanticRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/search-semantic.component').then(m => m.SearchSemanticComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/search-semantic-detail.component').then(m => m.SearchSemanticDetailComponent),
    resolve: {
      searchSemantic: SearchSemanticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/search-semantic-update.component').then(m => m.SearchSemanticUpdateComponent),
    resolve: {
      searchSemantic: SearchSemanticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/search-semantic-update.component').then(m => m.SearchSemanticUpdateComponent),
    resolve: {
      searchSemantic: SearchSemanticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default searchSemanticRoute;
