import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmailAttachmentResolve from './route/email-attachment-routing-resolve.service';

const emailAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-attachment.component').then(m => m.EmailAttachmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/email-attachment-detail.component').then(m => m.EmailAttachmentDetailComponent),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/email-attachment-update.component').then(m => m.EmailAttachmentUpdateComponent),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/email-attachment-update.component').then(m => m.EmailAttachmentUpdateComponent),
    resolve: {
      emailAttachment: EmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailAttachmentRoute;
