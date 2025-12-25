import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TagPredictionResolve from './route/tag-prediction-routing-resolve.service';

const tagPredictionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tag-prediction.component').then(m => m.TagPredictionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tag-prediction-detail.component').then(m => m.TagPredictionDetailComponent),
    resolve: {
      tagPrediction: TagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tag-prediction-update.component').then(m => m.TagPredictionUpdateComponent),
    resolve: {
      tagPrediction: TagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tag-prediction-update.component').then(m => m.TagPredictionUpdateComponent),
    resolve: {
      tagPrediction: TagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tagPredictionRoute;
