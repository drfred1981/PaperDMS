import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RedactionJobResolve from './route/redaction-job-routing-resolve.service';

const redactionJobRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/redaction-job.component').then(m => m.RedactionJobComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/redaction-job-detail.component').then(m => m.RedactionJobDetailComponent),
    resolve: {
      redactionJob: RedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/redaction-job-update.component').then(m => m.RedactionJobUpdateComponent),
    resolve: {
      redactionJob: RedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/redaction-job-update.component').then(m => m.RedactionJobUpdateComponent),
    resolve: {
      redactionJob: RedactionJobResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default redactionJobRoute;
