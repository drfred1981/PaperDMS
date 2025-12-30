import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageConversionConfigResolve from './route/image-conversion-config-routing-resolve.service';

const imageConversionConfigRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-conversion-config.component').then(m => m.ImageConversionConfigComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-conversion-config-detail.component').then(m => m.ImageConversionConfigDetailComponent),
    resolve: {
      imageConversionConfig: ImageConversionConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-conversion-config-update.component').then(m => m.ImageConversionConfigUpdateComponent),
    resolve: {
      imageConversionConfig: ImageConversionConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-conversion-config-update.component').then(m => m.ImageConversionConfigUpdateComponent),
    resolve: {
      imageConversionConfig: ImageConversionConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageConversionConfigRoute;
