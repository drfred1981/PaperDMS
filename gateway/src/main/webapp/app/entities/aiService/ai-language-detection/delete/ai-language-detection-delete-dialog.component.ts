import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAILanguageDetection } from '../ai-language-detection.model';
import { AILanguageDetectionService } from '../service/ai-language-detection.service';

@Component({
  templateUrl: './ai-language-detection-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AILanguageDetectionDeleteDialogComponent {
  aILanguageDetection?: IAILanguageDetection;

  protected aILanguageDetectionService = inject(AILanguageDetectionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aILanguageDetectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
