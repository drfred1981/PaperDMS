import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceStatus } from '../service-status.model';
import { ServiceStatusService } from '../service/service-status.service';

@Component({
  templateUrl: './service-status-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceStatusDeleteDialogComponent {
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
