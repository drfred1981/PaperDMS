import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AITagPredictionResolve from './route/ai-tag-prediction-routing-resolve.service';

const aITagPredictionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-tag-prediction.component').then(m => m.AITagPredictionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ai-tag-prediction-detail.component').then(m => m.AITagPredictionDetailComponent),
    resolve: {
      aITagPrediction: AITagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ai-tag-prediction-update.component').then(m => m.AITagPredictionUpdateComponent),
    resolve: {
      aITagPrediction: AITagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ai-tag-prediction-update.component').then(m => m.AITagPredictionUpdateComponent),
    resolve: {
      aITagPrediction: AITagPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aITagPredictionRoute;
