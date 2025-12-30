import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAITagPrediction } from '../ai-tag-prediction.model';
import { AITagPredictionService } from '../service/ai-tag-prediction.service';

@Component({
  templateUrl: './ai-tag-prediction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AITagPredictionDeleteDialogComponent {
  aITagPrediction?: IAITagPrediction;

  protected aITagPredictionService = inject(AITagPredictionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aITagPredictionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
