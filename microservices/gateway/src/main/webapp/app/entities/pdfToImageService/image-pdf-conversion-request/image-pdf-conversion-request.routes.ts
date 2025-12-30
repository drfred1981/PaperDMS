import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImagePdfConversionRequestResolve from './route/image-pdf-conversion-request-routing-resolve.service';

const imagePdfConversionRequestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image-pdf-conversion-request.component').then(m => m.ImagePdfConversionRequestComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/image-pdf-conversion-request-detail.component').then(m => m.ImagePdfConversionRequestDetailComponent),
    resolve: {
      imagePdfConversionRequest: ImagePdfConversionRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/image-pdf-conversion-request-update.component').then(m => m.ImagePdfConversionRequestUpdateComponent),
    resolve: {
      imagePdfConversionRequest: ImagePdfConversionRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/image-pdf-conversion-request-update.component').then(m => m.ImagePdfConversionRequestUpdateComponent),
    resolve: {
      imagePdfConversionRequest: ImagePdfConversionRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imagePdfConversionRequestRoute;
