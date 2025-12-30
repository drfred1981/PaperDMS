import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AIAutoTagJobResolve from './route/ai-auto-tag-job-routing-resolve.service';

const aIAutoTagJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-auto-tag-job.component').then(m => m.AIAutoTagJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-auto-tag-job-detail.component').then(m => m.AIAutoTagJobDetailComponent),
    resolve: {
      aIAutoTagJob: AIAutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-auto-tag-job-update.component').then(m => m.AIAutoTagJobUpdateComponent),
    resolve: {
      aIAutoTagJob: AIAutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-auto-tag-job-update.component').then(m => m.AIAutoTagJobUpdateComponent),
    resolve: {
      aIAutoTagJob: AIAutoTagJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aIAutoTagJobRoute;
