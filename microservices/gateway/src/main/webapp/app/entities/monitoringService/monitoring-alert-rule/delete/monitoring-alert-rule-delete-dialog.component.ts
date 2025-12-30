import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringAlertRule } from '../monitoring-alert-rule.model';
import { MonitoringAlertRuleService } from '../service/monitoring-alert-rule.service';

@Component({
  templateUrl: './monitoring-alert-rule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringAlertRuleDeleteDialogComponent {
  monitoringAlertRule?: IMonitoringAlertRule;

  protected monitoringAlertRuleService = inject(MonitoringAlertRuleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringAlertRuleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
