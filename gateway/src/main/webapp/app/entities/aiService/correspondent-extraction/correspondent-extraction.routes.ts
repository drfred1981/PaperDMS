import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CorrespondentExtractionResolve from './route/correspondent-extraction-routing-resolve.service';

const correspondentExtractionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/correspondent-extraction.component').then(m => m.CorrespondentExtractionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/correspondent-extraction-detail.component').then(m => m.CorrespondentExtractionDetailComponent),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/correspondent-extraction-update.component').then(m => m.CorrespondentExtractionUpdateComponent),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/correspondent-extraction-update.component').then(m => m.CorrespondentExtractionUpdateComponent),
    resolve: {
      correspondentExtraction: CorrespondentExtractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default correspondentExtractionRoute;
