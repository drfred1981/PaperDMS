import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OcrComparisonResolve from './route/ocr-comparison-routing-resolve.service';

const ocrComparisonRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-comparison.component').then(m => m.OcrComparisonComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-comparison-detail.component').then(m => m.OcrComparisonDetailComponent),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-comparison-update.component').then(m => m.OcrComparisonUpdateComponent),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-comparison-update.component').then(m => m.OcrComparisonUpdateComponent),
    resolve: {
      ocrComparison: OcrComparisonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrComparisonRoute;
