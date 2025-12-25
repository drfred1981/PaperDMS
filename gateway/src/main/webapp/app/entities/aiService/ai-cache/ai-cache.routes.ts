import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AiCacheResolve from './route/ai-cache-routing-resolve.service';

const aiCacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-cache.component').then(m => m.AiCacheComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-cache-detail.component').then(m => m.AiCacheDetailComponent),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-cache-update.component').then(m => m.AiCacheUpdateComponent),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-cache-update.component').then(m => m.AiCacheUpdateComponent),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aiCacheRoute;
