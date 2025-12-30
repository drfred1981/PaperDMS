import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageGeneratedImageResolve from './route/image-generated-image-routing-resolve.service';

const imageGeneratedImageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-generated-image.component').then(m => m.ImageGeneratedImageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-generated-image-detail.component').then(m => m.ImageGeneratedImageDetailComponent),
    resolve: {
      imageGeneratedImage: ImageGeneratedImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-generated-image-update.component').then(m => m.ImageGeneratedImageUpdateComponent),
    resolve: {
      imageGeneratedImage: ImageGeneratedImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-generated-image-update.component').then(m => m.ImageGeneratedImageUpdateComponent),
    resolve: {
      imageGeneratedImage: ImageGeneratedImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageGeneratedImageRoute;
