import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import OcrCacheResolve from './route/ocr-cache-routing-resolve.service';

const ocrCacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-cache').then(m => m.OcrCache),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-cache-detail').then(m => m.OcrCacheDetail),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-cache-update').then(m => m.OcrCacheUpdate),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-cache-update').then(m => m.OcrCacheUpdate),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrCacheRoute;
