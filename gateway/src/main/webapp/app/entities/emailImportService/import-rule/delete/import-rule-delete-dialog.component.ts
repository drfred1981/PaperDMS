import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImportRule } from '../import-rule.model';
import { ImportRuleService } from '../service/import-rule.service';

@Component({
  templateUrl: './import-rule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImportRuleDeleteDialogComponent {
  importRule?: IImportRule;

  protected importRuleService = inject(ImportRuleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.importRuleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
