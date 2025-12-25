import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BankStatementResolve from './route/bank-statement-routing-resolve.service';

const bankStatementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-statement.component').then(m => m.BankStatementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-statement-detail.component').then(m => m.BankStatementDetailComponent),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-statement-update.component').then(m => m.BankStatementUpdateComponent),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-statement-update.component').then(m => m.BankStatementUpdateComponent),
    resolve: {
      bankStatement: BankStatementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankStatementRoute;
