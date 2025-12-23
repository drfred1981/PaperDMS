import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ITag } from '../tag.model';

@Component({
  selector: 'jhi-tag-detail',
  templateUrl: './tag-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class TagDetail {
  tag = input<ITag | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
