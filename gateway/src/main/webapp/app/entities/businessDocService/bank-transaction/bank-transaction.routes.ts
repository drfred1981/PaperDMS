import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import BankTransactionResolve from './route/bank-transaction-routing-resolve.service';

const bankTransactionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank-transaction').then(m => m.BankTransaction),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-transaction-detail').then(m => m.BankTransactionDetail),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-transaction-update').then(m => m.BankTransactionUpdate),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-transaction-update').then(m => m.BankTransactionUpdate),
    resolve: {
      bankTransaction: BankTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankTransactionRoute;
