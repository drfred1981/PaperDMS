import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { ServiceStatusService } from '../service/service-status.service';
import { IServiceStatus } from '../service-status.model';

@Component({
  templateUrl: './service-status-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceStatusDeleteDialog {
  serviceStatus?: IServiceStatus;

  protected serviceStatusService = inject(ServiceStatusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
