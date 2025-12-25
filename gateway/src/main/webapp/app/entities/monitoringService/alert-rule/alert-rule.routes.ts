import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlertRuleResolve from './route/alert-rule-routing-resolve.service';

const alertRuleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alert-rule.component').then(m => m.AlertRuleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alert-rule-detail.component').then(m => m.AlertRuleDetailComponent),
    resolve: {
      alertRule: AlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alert-rule-update.component').then(m => m.AlertRuleUpdateComponent),
    resolve: {
      alertRule: AlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alert-rule-update.component').then(m => m.AlertRuleUpdateComponent),
    resolve: {
      alertRule: AlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alertRuleRoute;
