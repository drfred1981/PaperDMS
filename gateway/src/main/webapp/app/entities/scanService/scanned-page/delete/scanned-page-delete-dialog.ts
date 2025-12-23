import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IScannedPage } from '../scanned-page.model';
import { ScannedPageService } from '../service/scanned-page.service';

@Component({
  templateUrl: './scanned-page-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ScannedPageDeleteDialog {
  scannedPage?: IScannedPage;

  protected scannedPageService = inject(ScannedPageService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scannedPageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
