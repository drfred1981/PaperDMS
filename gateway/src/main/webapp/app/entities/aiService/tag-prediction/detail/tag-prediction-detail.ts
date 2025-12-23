import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ITagPrediction } from '../tag-prediction.model';

@Component({
  selector: 'jhi-tag-prediction-detail',
  templateUrl: './tag-prediction-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class TagPredictionDetail {
  tagPrediction = input<ITagPrediction | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
