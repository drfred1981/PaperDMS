import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExtractedFieldResolve from './route/extracted-field-routing-resolve.service';

const extractedFieldRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/extracted-field.component').then(m => m.ExtractedFieldComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/extracted-field-detail.component').then(m => m.ExtractedFieldDetailComponent),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/extracted-field-update.component').then(m => m.ExtractedFieldUpdateComponent),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/extracted-field-update.component').then(m => m.ExtractedFieldUpdateComponent),
    resolve: {
      extractedField: ExtractedFieldResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extractedFieldRoute;
