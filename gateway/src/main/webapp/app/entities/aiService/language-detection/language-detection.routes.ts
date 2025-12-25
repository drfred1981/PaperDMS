import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LanguageDetectionResolve from './route/language-detection-routing-resolve.service';

const languageDetectionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/language-detection.component').then(m => m.LanguageDetectionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/language-detection-detail.component').then(m => m.LanguageDetectionDetailComponent),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/language-detection-update.component').then(m => m.LanguageDetectionUpdateComponent),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/language-detection-update.component').then(m => m.LanguageDetectionUpdateComponent),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default languageDetectionRoute;
