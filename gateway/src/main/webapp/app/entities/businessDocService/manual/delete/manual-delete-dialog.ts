import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IManual } from '../manual.model';
import { ManualService } from '../service/manual.service';

@Component({
  templateUrl: './manual-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ManualDeleteDialog {
  manual?: IManual;

  protected manualService = inject(ManualService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
