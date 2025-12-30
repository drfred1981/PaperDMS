import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AITypePredictionResolve from './route/ai-type-prediction-routing-resolve.service';

const aITypePredictionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-type-prediction.component').then(m => m.AITypePredictionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-type-prediction-detail.component').then(m => m.AITypePredictionDetailComponent),
    resolve: {
      aITypePrediction: AITypePredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-type-prediction-update.component').then(m => m.AITypePredictionUpdateComponent),
    resolve: {
      aITypePrediction: AITypePredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-type-prediction-update.component').then(m => m.AITypePredictionUpdateComponent),
    resolve: {
      aITypePrediction: AITypePredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aITypePredictionRoute;
