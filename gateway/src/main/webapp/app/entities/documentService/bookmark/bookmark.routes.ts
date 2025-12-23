import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import BookmarkResolve from './route/bookmark-routing-resolve.service';

const bookmarkRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bookmark').then(m => m.Bookmark),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bookmark-detail').then(m => m.BookmarkDetail),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bookmark-update').then(m => m.BookmarkUpdate),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bookmark-update').then(m => m.BookmarkUpdate),
    resolve: {
      bookmark: BookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookmarkRoute;
