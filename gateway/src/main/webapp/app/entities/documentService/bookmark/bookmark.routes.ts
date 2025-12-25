import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BookmarkResolve from './route/bookmark-routing-resolve.service';

const bookmarkRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bookmark.component').then(m => m.BookmarkComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bookmark-detail.component').then(m => m.BookmarkDetailComponent),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bookmark-update.component').then(m => m.BookmarkUpdateComponent),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bookmark-update.component').then(m => m.BookmarkUpdateComponent),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookmarkRoute;
