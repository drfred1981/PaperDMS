import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import EmailAttachmentResolve from './route/email-attachment-routing-resolve.service';

const emailAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-attachment').then(m => m.EmailAttachment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/email-attachment-detail').then(m => m.EmailAttachmentDetail),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/email-attachment-update').then(m => m.EmailAttachmentUpdate),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/email-attachment-update').then(m => m.EmailAttachmentUpdate),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailAttachmentRoute;
