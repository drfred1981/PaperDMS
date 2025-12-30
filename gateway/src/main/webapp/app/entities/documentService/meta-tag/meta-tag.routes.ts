import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaTagResolve from './route/meta-tag-routing-resolve.service';

const metaTagRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-tag.component').then(m => m.MetaTagComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-tag-detail.component').then(m => m.MetaTagDetailComponent),
    resolve: {
      metaTag: MetaTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-tag-update.component').then(m => m.MetaTagUpdateComponent),
    resolve: {
      metaTag: MetaTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-tag-update.component').then(m => m.MetaTagUpdateComponent),
    resolve: {
      metaTag: MetaTagResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaTagRoute;
