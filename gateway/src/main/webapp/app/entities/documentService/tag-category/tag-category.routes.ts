import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TagCategoryResolve from './route/tag-category-routing-resolve.service';

const tagCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tag-category').then(m => m.TagCategory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tag-category-detail').then(m => m.TagCategoryDetail),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tag-category-update').then(m => m.TagCategoryUpdate),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tag-category-update').then(m => m.TagCategoryUpdate),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tagCategoryRoute;
