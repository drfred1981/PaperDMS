import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IAlert } from '../alert.model';
import { AlertService } from '../service/alert.service';

@Component({
  templateUrl: './alert-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class AlertDeleteDialog {
  alert?: IAlert;

  protected alertService = inject(AlertService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alertService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
