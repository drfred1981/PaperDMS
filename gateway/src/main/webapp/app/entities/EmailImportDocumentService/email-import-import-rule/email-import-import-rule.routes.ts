import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmailImportImportRuleResolve from './route/email-import-import-rule-routing-resolve.service';

const emailImportImportRuleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-import-import-rule.component').then(m => m.EmailImportImportRuleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/email-import-import-rule-detail.component').then(m => m.EmailImportImportRuleDetailComponent),
    resolve: {
      emailImportImportRule: EmailImportImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/email-import-import-rule-update.component').then(m => m.EmailImportImportRuleUpdateComponent),
    resolve: {
      emailImportImportRule: EmailImportImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/email-import-import-rule-update.component').then(m => m.EmailImportImportRuleUpdateComponent),
    resolve: {
      emailImportImportRule: EmailImportImportRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailImportImportRuleRoute;
