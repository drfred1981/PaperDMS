import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExtractedTextResolve from './route/extracted-text-routing-resolve.service';

const extractedTextRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/extracted-text').then(m => m.ExtractedText),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/extracted-text-detail').then(m => m.ExtractedTextDetail),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/extracted-text-update').then(m => m.ExtractedTextUpdate),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/extracted-text-update').then(m => m.ExtractedTextUpdate),
    resolve: {
      extractedText: ExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extractedTextRoute;
