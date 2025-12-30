import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmailImportImportMapping } from '../email-import-import-mapping.model';
import { EmailImportImportMappingService } from '../service/email-import-import-mapping.service';

@Component({
  templateUrl: './email-import-import-mapping-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportImportMappingDeleteDialogComponent {
  emailImportImportMapping?: IEmailImportImportMapping;

  protected emailImportImportMappingService = inject(EmailImportImportMappingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailImportImportMappingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
