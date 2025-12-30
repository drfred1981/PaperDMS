import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrcExtractedText } from '../orc-extracted-text.model';
import { OrcExtractedTextService } from '../service/orc-extracted-text.service';

@Component({
  templateUrl: './orc-extracted-text-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrcExtractedTextDeleteDialogComponent {
  orcExtractedText?: IOrcExtractedText;

  protected orcExtractedTextService = inject(OrcExtractedTextService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orcExtractedTextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
