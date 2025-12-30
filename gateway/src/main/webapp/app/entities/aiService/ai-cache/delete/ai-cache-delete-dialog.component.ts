import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAICache } from '../ai-cache.model';
import { AICacheService } from '../service/ai-cache.service';

@Component({
  templateUrl: './ai-cache-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AICacheDeleteDialogComponent {
  aICache?: IAICache;

  protected aICacheService = inject(AICacheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aICacheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
