import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import OcrComparisonResolve from './route/ocr-comparison-routing-resolve.service';

const ocrComparisonRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-comparison').then(m => m.OcrComparison),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-comparison-detail').then(m => m.OcrComparisonDetail),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-comparison-update').then(m => m.OcrComparisonUpdate),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-comparison-update').then(m => m.OcrComparisonUpdate),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrComparisonRoute;
