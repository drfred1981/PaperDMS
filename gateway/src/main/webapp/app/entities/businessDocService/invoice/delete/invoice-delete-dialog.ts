import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';

@Component({
  templateUrl: './invoice-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class InvoiceDeleteDialog {
  invoice?: IInvoice;

  protected invoiceService = inject(InvoiceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
