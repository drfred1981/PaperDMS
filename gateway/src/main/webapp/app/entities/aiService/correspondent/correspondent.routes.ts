import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CorrespondentResolve from './route/correspondent-routing-resolve.service';

const correspondentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/correspondent').then(m => m.Correspondent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/correspondent-detail').then(m => m.CorrespondentDetail),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/correspondent-update').then(m => m.CorrespondentUpdate),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/correspondent-update').then(m => m.CorrespondentUpdate),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default correspondentRoute;
