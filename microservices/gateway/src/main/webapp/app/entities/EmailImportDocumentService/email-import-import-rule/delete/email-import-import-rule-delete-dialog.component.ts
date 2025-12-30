import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmailImportImportRule } from '../email-import-import-rule.model';
import { EmailImportImportRuleService } from '../service/email-import-import-rule.service';

@Component({
  templateUrl: './email-import-import-rule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmailImportImportRuleDeleteDialogComponent {
  emailImportImportRule?: IEmailImportImportRule;

  protected emailImportImportRuleService = inject(EmailImportImportRuleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailImportImportRuleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
