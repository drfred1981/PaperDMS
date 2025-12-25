import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExtractedTextResolve from './route/extracted-text-routing-resolve.service';

const extractedTextRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/extracted-text.component').then(m => m.ExtractedTextComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/extracted-text-detail.component').then(m => m.ExtractedTextDetailComponent),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/extracted-text-update.component').then(m => m.ExtractedTextUpdateComponent),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/extracted-text-update.component').then(m => m.ExtractedTextUpdateComponent),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extractedTextRoute;
