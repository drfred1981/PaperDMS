import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ManualChapterResolve from './route/manual-chapter-routing-resolve.service';

const manualChapterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/manual-chapter').then(m => m.ManualChapter),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/manual-chapter-detail').then(m => m.ManualChapterDetail),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/manual-chapter-update').then(m => m.ManualChapterUpdate),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/manual-chapter-update').then(m => m.ManualChapterUpdate),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default manualChapterRoute;
