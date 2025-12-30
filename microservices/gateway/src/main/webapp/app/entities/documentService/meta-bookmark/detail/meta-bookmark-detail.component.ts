import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMetaBookmark } from '../meta-bookmark.model';

@Component({
  selector: 'jhi-meta-bookmark-detail',
  templateUrl: './meta-bookmark-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MetaBookmarkDetailComponent {
  metaBookmark = input<IMetaBookmark | null>(null);

  previousState(): void {
    window.history.back();
  }
}
