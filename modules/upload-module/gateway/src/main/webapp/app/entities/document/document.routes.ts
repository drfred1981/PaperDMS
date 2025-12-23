import { Routes } from '@angular/router';
import { DocumentUploadComponent } from './upload/document-upload.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

/**
 * Routes for document management.
 * Uses JHipster's route.ts pattern (not routing modules).
 */
export const documentRoutes: Routes = [
  {
    path: 'upload',
    component: DocumentUploadComponent,
    canActivate: [UserRouteAccessService],
    data: {
      pageTitle: 'Upload Documents'
    }
  }
];
