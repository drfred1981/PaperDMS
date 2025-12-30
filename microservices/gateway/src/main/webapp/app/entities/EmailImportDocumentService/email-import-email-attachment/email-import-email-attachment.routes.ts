import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmailImportEmailAttachmentResolve from './route/email-import-email-attachment-routing-resolve.service';

const emailImportEmailAttachmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/email-import-email-attachment.component').then(m => m.EmailImportEmailAttachmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/email-import-email-attachment-detail.component').then(m => m.EmailImportEmailAttachmentDetailComponent),
    resolve: {
      emailImportEmailAttachment: EmailImportEmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/email-import-email-attachment-update.component').then(m => m.EmailImportEmailAttachmentUpdateComponent),
    resolve: {
      emailImportEmailAttachment: EmailImportEmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/email-import-email-attachment-update.component').then(m => m.EmailImportEmailAttachmentUpdateComponent),
    resolve: {
      emailImportEmailAttachment: EmailImportEmailAttachmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default emailImportEmailAttachmentRoute;
