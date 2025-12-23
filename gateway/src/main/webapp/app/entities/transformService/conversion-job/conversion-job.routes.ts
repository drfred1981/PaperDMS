import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ConversionJobResolve from './route/conversion-job-routing-resolve.service';

const conversionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/conversion-job').then(m => m.ConversionJob),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/conversion-job-detail').then(m => m.ConversionJobDetail),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/conversion-job-update').then(m => m.ConversionJobUpdate),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/conversion-job-update').then(m => m.ConversionJobUpdate),
    resolve: {
      conversionJob: ConversionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default conversionJobRoute;
