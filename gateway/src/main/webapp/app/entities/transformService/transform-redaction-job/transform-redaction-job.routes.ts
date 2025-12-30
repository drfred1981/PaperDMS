import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransformRedactionJobResolve from './route/transform-redaction-job-routing-resolve.service';

const transformRedactionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transform-redaction-job.component').then(m => m.TransformRedactionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transform-redaction-job-detail.component').then(m => m.TransformRedactionJobDetailComponent),
    resolve: {
      transformRedactionJob: TransformRedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transform-redaction-job-update.component').then(m => m.TransformRedactionJobUpdateComponent),
    resolve: {
      transformRedactionJob: TransformRedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transform-redaction-job-update.component').then(m => m.TransformRedactionJobUpdateComponent),
    resolve: {
      transformRedactionJob: TransformRedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transformRedactionJobRoute;
