import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IAlertRule } from '../alert-rule.model';
import { AlertRuleService } from '../service/alert-rule.service';

@Component({
  templateUrl: './alert-rule-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class AlertRuleDeleteDialog {
  alertRule?: IAlertRule;

  protected alertRuleService = inject(AlertRuleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alertRuleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
