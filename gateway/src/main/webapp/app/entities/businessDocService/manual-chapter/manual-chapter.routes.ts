import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ManualChapterResolve from './route/manual-chapter-routing-resolve.service';

const manualChapterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/manual-chapter.component').then(m => m.ManualChapterComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/manual-chapter-detail.component').then(m => m.ManualChapterDetailComponent),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/manual-chapter-update.component').then(m => m.ManualChapterUpdateComponent),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/manual-chapter-update.component').then(m => m.ManualChapterUpdateComponent),
    resolve: {
      manualChapter: ManualChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default manualChapterRoute;
