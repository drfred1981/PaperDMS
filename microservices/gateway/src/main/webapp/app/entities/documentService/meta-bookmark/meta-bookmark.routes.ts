import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaBookmarkResolve from './route/meta-bookmark-routing-resolve.service';

const metaBookmarkRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-bookmark.component').then(m => m.MetaBookmarkComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-bookmark-detail.component').then(m => m.MetaBookmarkDetailComponent),
    resolve: {
      metaBookmark: MetaBookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-bookmark-update.component').then(m => m.MetaBookmarkUpdateComponent),
    resolve: {
      metaBookmark: MetaBookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-bookmark-update.component').then(m => m.MetaBookmarkUpdateComponent),
    resolve: {
      metaBookmark: MetaBookmarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaBookmarkRoute;
