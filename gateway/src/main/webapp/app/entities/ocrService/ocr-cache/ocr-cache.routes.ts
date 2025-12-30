import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OcrCacheResolve from './route/ocr-cache-routing-resolve.service';

const ocrCacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-cache.component').then(m => m.OcrCacheComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-cache-detail.component').then(m => m.OcrCacheDetailComponent),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-cache-update.component').then(m => m.OcrCacheUpdateComponent),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-cache-update.component').then(m => m.OcrCacheUpdateComponent),
    resolve: {
      ocrCache: OcrCacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrCacheRoute;
