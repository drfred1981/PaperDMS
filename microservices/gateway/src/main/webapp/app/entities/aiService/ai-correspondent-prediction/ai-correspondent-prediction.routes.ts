import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AICorrespondentPredictionResolve from './route/ai-correspondent-prediction-routing-resolve.service';

const aICorrespondentPredictionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ai-correspondent-prediction.component').then(m => m.AICorrespondentPredictionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/ai-correspondent-prediction-detail.component').then(m => m.AICorrespondentPredictionDetailComponent),
    resolve: {
      aICorrespondentPrediction: AICorrespondentPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/ai-correspondent-prediction-update.component').then(m => m.AICorrespondentPredictionUpdateComponent),
    resolve: {
      aICorrespondentPrediction: AICorrespondentPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/ai-correspondent-prediction-update.component').then(m => m.AICorrespondentPredictionUpdateComponent),
    resolve: {
      aICorrespondentPrediction: AICorrespondentPredictionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aICorrespondentPredictionRoute;
