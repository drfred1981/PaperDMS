import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import InvoiceLineResolve from './route/invoice-line-routing-resolve.service';

const invoiceLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/invoice-line').then(m => m.InvoiceLine),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/invoice-line-detail').then(m => m.InvoiceLineDetail),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/invoice-line-update').then(m => m.InvoiceLineUpdate),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/invoice-line-update').then(m => m.InvoiceLineUpdate),
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default invoiceLineRoute;
