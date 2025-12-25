import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISystemHealth } from '../system-health.model';
import { SystemHealthService } from '../service/system-health.service';

@Component({
  templateUrl: './system-health-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SystemHealthDeleteDialogComponent {
  systemHealth?: ISystemHealth;

  protected systemHealthService = inject(SystemHealthService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemHealthService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
