import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CorrespondentResolve from './route/correspondent-routing-resolve.service';

const correspondentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/correspondent.component').then(m => m.CorrespondentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/correspondent-detail.component').then(m => m.CorrespondentDetailComponent),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/correspondent-update.component').then(m => m.CorrespondentUpdateComponent),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/correspondent-update.component').then(m => m.CorrespondentUpdateComponent),
    resolve: {
      correspondent: CorrespondentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default correspondentRoute;
