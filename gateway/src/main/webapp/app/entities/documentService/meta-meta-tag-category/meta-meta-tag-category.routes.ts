import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MetaMetaTagCategoryResolve from './route/meta-meta-tag-category-routing-resolve.service';

const metaMetaTagCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meta-meta-tag-category.component').then(m => m.MetaMetaTagCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/meta-meta-tag-category-detail.component').then(m => m.MetaMetaTagCategoryDetailComponent),
    resolve: {
      metaMetaTagCategory: MetaMetaTagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meta-meta-tag-category-update.component').then(m => m.MetaMetaTagCategoryUpdateComponent),
    resolve: {
      metaMetaTagCategory: MetaMetaTagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meta-meta-tag-category-update.component').then(m => m.MetaMetaTagCategoryUpdateComponent),
    resolve: {
      metaMetaTagCategory: MetaMetaTagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metaMetaTagCategoryRoute;
