import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ImportRuleResolve from './route/import-rule-routing-resolve.service';

const importRuleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/import-rule').then(m => m.ImportRule),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/import-rule-detail').then(m => m.ImportRuleDetail),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/import-rule-update').then(m => m.ImportRuleUpdate),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/import-rule-update').then(m => m.ImportRuleUpdate),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default importRuleRoute;
