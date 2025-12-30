import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaSavedSearchResolve from './route/meta-saved-search-routing-resolve.service';

const metaSavedSearchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-saved-search.component').then(m => m.MetaSavedSearchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-saved-search-detail.component').then(m => m.MetaSavedSearchDetailComponent),
    resolve: {
      metaSavedSearch: MetaSavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-saved-search-update.component').then(m => m.MetaSavedSearchUpdateComponent),
    resolve: {
      metaSavedSearch: MetaSavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-saved-search-update.component').then(m => m.MetaSavedSearchUpdateComponent),
    resolve: {
      metaSavedSearch: MetaSavedSearchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaSavedSearchRoute;
