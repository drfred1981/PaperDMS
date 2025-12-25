import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TagCategoryResolve from './route/tag-category-routing-resolve.service';

const tagCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tag-category.component').then(m => m.TagCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tag-category-detail.component').then(m => m.TagCategoryDetailComponent),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tag-category-update.component').then(m => m.TagCategoryUpdateComponent),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tag-category-update.component').then(m => m.TagCategoryUpdateComponent),
    resolve: {
      tagCategory: TagCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tagCategoryRoute;
