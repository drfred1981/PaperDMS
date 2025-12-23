import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentPermission } from '../document-permission.model';

@Component({
  selector: 'jhi-document-permission-detail',
  templateUrl: './document-permission-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentPermissionDetail {
  documentPermission = input<IDocumentPermission | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
