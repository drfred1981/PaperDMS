import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMetaTag } from '../meta-tag.model';

@Component({
  selector: 'jhi-meta-tag-detail',
  templateUrl: './meta-tag-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MetaTagDetailComponent {
  metaTag = input<IMetaTag | null>(null);

  previousState(): void {
    window.history.back();
  }
}
