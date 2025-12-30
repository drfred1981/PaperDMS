import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AICacheResolve from './route/ai-cache-routing-resolve.service';

const aICacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-cache.component').then(m => m.AICacheComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-cache-detail.component').then(m => m.AICacheDetailComponent),
    resolve: {
      aICache: AICacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-cache-update.component').then(m => m.AICacheUpdateComponent),
    resolve: {
      aICache: AICacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-cache-update.component').then(m => m.AICacheUpdateComponent),
    resolve: {
      aICache: AICacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aICacheRoute;
