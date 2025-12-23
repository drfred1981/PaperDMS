import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import BankStatementResolve from './route/bank-statement-routing-resolve.service';

const bankStatementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-statement').then(m => m.BankStatement),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-statement-detail').then(m => m.BankStatementDetail),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-statement-update').then(m => m.BankStatementUpdate),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-statement-update').then(m => m.BankStatementUpdate),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankStatementRoute;
