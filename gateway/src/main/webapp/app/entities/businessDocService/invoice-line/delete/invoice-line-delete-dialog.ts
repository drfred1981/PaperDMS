import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineService } from '../service/invoice-line.service';

@Component({
  templateUrl: './invoice-line-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class InvoiceLineDeleteDialog {
  invoiceLine?: IInvoiceLine;

  protected invoiceLineService = inject(InvoiceLineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceLineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
