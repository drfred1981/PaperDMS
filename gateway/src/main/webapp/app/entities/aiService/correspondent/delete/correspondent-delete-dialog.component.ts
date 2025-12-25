import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICorrespondent } from '../correspondent.model';
import { CorrespondentService } from '../service/correspondent.service';

@Component({
  templateUrl: './correspondent-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CorrespondentDeleteDialogComponent {
  correspondent?: ICorrespondent;

  protected correspondentService = inject(CorrespondentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.correspondentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
