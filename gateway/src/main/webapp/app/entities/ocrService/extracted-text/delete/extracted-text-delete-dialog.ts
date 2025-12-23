import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IExtractedText } from '../extracted-text.model';
import { ExtractedTextService } from '../service/extracted-text.service';

@Component({
  templateUrl: './extracted-text-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ExtractedTextDeleteDialog {
  extractedText?: IExtractedText;

  protected extractedTextService = inject(ExtractedTextService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extractedTextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
