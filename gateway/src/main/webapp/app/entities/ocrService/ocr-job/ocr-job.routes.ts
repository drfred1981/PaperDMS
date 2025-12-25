import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OcrJobResolve from './route/ocr-job-routing-resolve.service';

const ocrJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ocr-job.component').then(m => m.OcrJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ocr-job-detail.component').then(m => m.OcrJobDetailComponent),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ocr-job-update.component').then(m => m.OcrJobUpdateComponent),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ocr-job-update.component').then(m => m.OcrJobUpdateComponent),
    resolve: {
      ocrJob: OcrJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ocrJobRoute;
