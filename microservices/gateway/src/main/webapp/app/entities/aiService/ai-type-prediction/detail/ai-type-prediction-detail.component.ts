import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAITypePrediction } from '../ai-type-prediction.model';

@Component({
  selector: 'jhi-ai-type-prediction-detail',
  templateUrl: './ai-type-prediction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AITypePredictionDetailComponent {
  aITypePrediction = input<IAITypePrediction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
