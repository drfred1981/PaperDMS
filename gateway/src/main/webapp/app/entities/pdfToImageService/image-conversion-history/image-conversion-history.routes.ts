import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageConversionHistoryResolve from './route/image-conversion-history-routing-resolve.service';

const imageConversionHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-conversion-history.component').then(m => m.ImageConversionHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-conversion-history-detail.component').then(m => m.ImageConversionHistoryDetailComponent),
    resolve: {
      imageConversionHistory: ImageConversionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-conversion-history-update.component').then(m => m.ImageConversionHistoryUpdateComponent),
    resolve: {
      imageConversionHistory: ImageConversionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-conversion-history-update.component').then(m => m.ImageConversionHistoryUpdateComponent),
    resolve: {
      imageConversionHistory: ImageConversionHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageConversionHistoryRoute;
