import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentPermission } from '../document-permission.model';

@Component({
  selector: 'jhi-document-permission-detail',
  templateUrl: './document-permission-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentPermissionDetailComponent {
  documentPermission = input<IDocumentPermission | null>(null);

  previousState(): void {
    window.history.back();
  }
}
