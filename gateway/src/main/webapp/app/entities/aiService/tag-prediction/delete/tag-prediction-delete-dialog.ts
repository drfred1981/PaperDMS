import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { TagPredictionService } from '../service/tag-prediction.service';
import { ITagPrediction } from '../tag-prediction.model';

@Component({
  templateUrl: './tag-prediction-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class TagPredictionDeleteDialog {
  tagPrediction?: ITagPrediction;

  protected tagPredictionService = inject(TagPredictionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagPredictionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
