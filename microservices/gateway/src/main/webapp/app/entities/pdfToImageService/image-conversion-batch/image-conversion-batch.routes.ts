import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageConversionBatchResolve from './route/image-conversion-batch-routing-resolve.service';

const imageConversionBatchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-conversion-batch.component').then(m => m.ImageConversionBatchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-conversion-batch-detail.component').then(m => m.ImageConversionBatchDetailComponent),
    resolve: {
      imageConversionBatch: ImageConversionBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-conversion-batch-update.component').then(m => m.ImageConversionBatchUpdateComponent),
    resolve: {
      imageConversionBatch: ImageConversionBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-conversion-batch-update.component').then(m => m.ImageConversionBatchUpdateComponent),
    resolve: {
      imageConversionBatch: ImageConversionBatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageConversionBatchRoute;
