import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAiCache } from '../ai-cache.model';
import { AiCacheService } from '../service/ai-cache.service';

@Component({
  templateUrl: './ai-cache-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AiCacheDeleteDialogComponent {
  aiCache?: IAiCache;

  protected aiCacheService = inject(AiCacheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aiCacheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
