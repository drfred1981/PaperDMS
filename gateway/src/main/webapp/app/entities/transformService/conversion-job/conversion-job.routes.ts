import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ConversionJobResolve from './route/conversion-job-routing-resolve.service';

const conversionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/conversion-job.component').then(m => m.ConversionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/conversion-job-detail.component').then(m => m.ConversionJobDetailComponent),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/conversion-job-update.component').then(m => m.ConversionJobUpdateComponent),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/conversion-job-update.component').then(m => m.ConversionJobUpdateComponent),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default conversionJobRoute;
