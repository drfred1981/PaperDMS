import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IScannerConfiguration } from '../scanner-configuration.model';
import { ScannerConfigurationService } from '../service/scanner-configuration.service';

@Component({
  templateUrl: './scanner-configuration-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ScannerConfigurationDeleteDialogComponent {
  scannerConfiguration?: IScannerConfiguration;

  protected scannerConfigurationService = inject(ScannerConfigurationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scannerConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
