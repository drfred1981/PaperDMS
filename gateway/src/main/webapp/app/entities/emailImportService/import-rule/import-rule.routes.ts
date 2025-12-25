import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImportRuleResolve from './route/import-rule-routing-resolve.service';

const importRuleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/import-rule.component').then(m => m.ImportRuleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/import-rule-detail.component').then(m => m.ImportRuleDetailComponent),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/import-rule-update.component').then(m => m.ImportRuleUpdateComponent),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/import-rule-update.component').then(m => m.ImportRuleUpdateComponent),
    resolve: {
      importRule: ImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default importRuleRoute;
