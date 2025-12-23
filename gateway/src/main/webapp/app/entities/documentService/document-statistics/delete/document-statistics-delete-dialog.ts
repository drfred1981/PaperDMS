import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsService } from '../service/document-statistics.service';

@Component({
  templateUrl: './document-statistics-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentStatisticsDeleteDialog {
  documentStatistics?: IDocumentStatistics;

  protected documentStatisticsService = inject(DocumentStatisticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentStatisticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
