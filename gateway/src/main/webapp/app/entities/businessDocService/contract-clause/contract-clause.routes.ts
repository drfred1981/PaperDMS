import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ContractClauseResolve from './route/contract-clause-routing-resolve.service';

const contractClauseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contract-clause.component').then(m => m.ContractClauseComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contract-clause-detail.component').then(m => m.ContractClauseDetailComponent),
    resolve: {
      contractClause: ContractClauseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contract-clause-update.component').then(m => m.ContractClauseUpdateComponent),
    resolve: {
      contractClause: ContractClauseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contract-clause-update.component').then(m => m.ContractClauseUpdateComponent),
    resolve: {
      contractClause: ContractClauseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractClauseRoute;
