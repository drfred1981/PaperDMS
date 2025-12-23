import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExtractedFieldResolve from './route/extracted-field-routing-resolve.service';

const extractedFieldRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/extracted-field').then(m => m.ExtractedField),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/extracted-field-detail').then(m => m.ExtractedFieldDetail),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/extracted-field-update').then(m => m.ExtractedFieldUpdate),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/extracted-field-update').then(m => m.ExtractedFieldUpdate),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extractedFieldRoute;
