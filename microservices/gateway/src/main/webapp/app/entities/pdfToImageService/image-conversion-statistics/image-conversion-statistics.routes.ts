import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageConversionStatisticsResolve from './route/image-conversion-statistics-routing-resolve.service';

const imageConversionStatisticsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-conversion-statistics.component').then(m => m.ImageConversionStatisticsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/image-conversion-statistics-detail.component').then(m => m.ImageConversionStatisticsDetailComponent),
    resolve: {
      imageConversionStatistics: ImageConversionStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/image-conversion-statistics-update.component').then(m => m.ImageConversionStatisticsUpdateComponent),
    resolve: {
      imageConversionStatistics: ImageConversionStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/image-conversion-statistics-update.component').then(m => m.ImageConversionStatisticsUpdateComponent),
    resolve: {
      imageConversionStatistics: ImageConversionStatisticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageConversionStatisticsRoute;
