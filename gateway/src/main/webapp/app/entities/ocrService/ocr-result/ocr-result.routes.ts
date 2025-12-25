import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OcrResultResolve from './route/ocr-result-routing-resolve.service';

const ocrResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-result.component').then(m => m.OcrResultComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-result-detail.component').then(m => m.OcrResultDetailComponent),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-result-update.component').then(m => m.OcrResultUpdateComponent),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-result-update.component').then(m => m.OcrResultUpdateComponent),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrResultRoute;
