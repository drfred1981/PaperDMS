import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import LanguageDetectionResolve from './route/language-detection-routing-resolve.service';

const languageDetectionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/language-detection').then(m => m.LanguageDetection),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/language-detection-detail').then(m => m.LanguageDetectionDetail),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/language-detection-update').then(m => m.LanguageDetectionUpdate),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/language-detection-update').then(m => m.LanguageDetectionUpdate),
    resolve: {
      languageDetection: LanguageDetectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default languageDetectionRoute;
