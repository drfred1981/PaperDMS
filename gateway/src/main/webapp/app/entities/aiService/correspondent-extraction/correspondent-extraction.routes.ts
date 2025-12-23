import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CorrespondentExtractionResolve from './route/correspondent-extraction-routing-resolve.service';

const correspondentExtractionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/correspondent-extraction').then(m => m.CorrespondentExtraction),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/correspondent-extraction-detail').then(m => m.CorrespondentExtractionDetail),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/correspondent-extraction-update').then(m => m.CorrespondentExtractionUpdate),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/correspondent-extraction-update').then(m => m.CorrespondentExtractionUpdate),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default correspondentExtractionRoute;
