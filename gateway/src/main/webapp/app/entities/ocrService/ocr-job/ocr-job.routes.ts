import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import OcrJobResolve from './route/ocr-job-routing-resolve.service';

const ocrJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-job').then(m => m.OcrJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-job-detail').then(m => m.OcrJobDetail),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-job-update').then(m => m.OcrJobUpdate),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-job-update').then(m => m.OcrJobUpdate),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrJobRoute;
