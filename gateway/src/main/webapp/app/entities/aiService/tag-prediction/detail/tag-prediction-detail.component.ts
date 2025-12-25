import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITagPrediction } from '../tag-prediction.model';

@Component({
  selector: 'jhi-tag-prediction-detail',
  templateUrl: './tag-prediction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TagPredictionDetailComponent {
  tagPrediction = input<ITagPrediction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
