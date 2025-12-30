import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MonitoringAlertRuleResolve from './route/monitoring-alert-rule-routing-resolve.service';

const monitoringAlertRuleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitoring-alert-rule.component').then(m => m.MonitoringAlertRuleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitoring-alert-rule-detail.component').then(m => m.MonitoringAlertRuleDetailComponent),
    resolve: {
      monitoringAlertRule: MonitoringAlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitoring-alert-rule-update.component').then(m => m.MonitoringAlertRuleUpdateComponent),
    resolve: {
      monitoringAlertRule: MonitoringAlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitoring-alert-rule-update.component').then(m => m.MonitoringAlertRuleUpdateComponent),
    resolve: {
      monitoringAlertRule: MonitoringAlertRuleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitoringAlertRuleRoute;
