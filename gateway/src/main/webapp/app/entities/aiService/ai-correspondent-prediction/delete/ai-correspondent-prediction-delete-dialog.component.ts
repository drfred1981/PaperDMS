import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import { AICorrespondentPredictionService } from '../service/ai-correspondent-prediction.service';

@Component({
  templateUrl: './ai-correspondent-prediction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AICorrespondentPredictionDeleteDialogComponent {
  aICorrespondentPrediction?: IAICorrespondentPrediction;

  protected aICorrespondentPredictionService = inject(AICorrespondentPredictionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aICorrespondentPredictionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
