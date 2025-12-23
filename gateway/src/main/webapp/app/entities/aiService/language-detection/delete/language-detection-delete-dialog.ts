import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ILanguageDetection } from '../language-detection.model';
import { LanguageDetectionService } from '../service/language-detection.service';

@Component({
  templateUrl: './language-detection-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class LanguageDetectionDeleteDialog {
  languageDetection?: ILanguageDetection;

  protected languageDetectionService = inject(LanguageDetectionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.languageDetectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
