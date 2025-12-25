import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImportMapping } from '../import-mapping.model';
import { ImportMappingService } from '../service/import-mapping.service';

@Component({
  templateUrl: './import-mapping-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImportMappingDeleteDialogComponent {
  importMapping?: IImportMapping;

  protected importMappingService = inject(ImportMappingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.importMappingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
