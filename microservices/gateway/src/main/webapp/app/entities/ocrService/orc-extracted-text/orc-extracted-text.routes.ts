import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OrcExtractedTextResolve from './route/orc-extracted-text-routing-resolve.service';

const orcExtractedTextRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/orc-extracted-text.component').then(m => m.OrcExtractedTextComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/orc-extracted-text-detail.component').then(m => m.OrcExtractedTextDetailComponent),
    resolve: {
      orcExtractedText: OrcExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/orc-extracted-text-update.component').then(m => m.OrcExtractedTextUpdateComponent),
    resolve: {
      orcExtractedText: OrcExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/orc-extracted-text-update.component').then(m => m.OrcExtractedTextUpdateComponent),
    resolve: {
      orcExtractedText: OrcExtractedTextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orcExtractedTextRoute;
