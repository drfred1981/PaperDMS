import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IBookmark } from '../bookmark.model';

@Component({
  selector: 'jhi-bookmark-detail',
  templateUrl: './bookmark-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class BookmarkDetail {
  bookmark = input<IBookmark | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
