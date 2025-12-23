import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import OcrResultResolve from './route/ocr-result-routing-resolve.service';

const ocrResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-result').then(m => m.OcrResult),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-result-detail').then(m => m.OcrResultDetail),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-result-update').then(m => m.OcrResultUpdate),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-result-update').then(m => m.OcrResultUpdate),
    resolve: {
      ocrResult: OcrResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrResultRoute;
