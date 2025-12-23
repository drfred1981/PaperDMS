import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import AiCacheResolve from './route/ai-cache-routing-resolve.service';

const aiCacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-cache').then(m => m.AiCache),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-cache-detail').then(m => m.AiCacheDetail),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-cache-update').then(m => m.AiCacheUpdate),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-cache-update').then(m => m.AiCacheUpdate),
    resolve: {
      aiCache: AiCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aiCacheRoute;
