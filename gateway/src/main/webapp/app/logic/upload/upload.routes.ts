import { Routes } from '@angular/router';
import { DocumentUploadComponent } from './document-upload.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

/**
 * Routes for document management.
 * Uses JHipster's route.ts pattern (not routing modules).
 */
const uploadRoutes: Routes = [
  {
    path: 'upload',
    component: DocumentUploadComponent,
    canActivate: [UserRouteAccessService],
    data: {
      pageTitle: 'Upload Documents'
    }
  }
];
export default uploadRoutes;
