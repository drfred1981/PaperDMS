import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBookmark } from '../bookmark.model';

@Component({
  selector: 'jhi-bookmark-detail',
  templateUrl: './bookmark-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BookmarkDetailComponent {
  bookmark = input<IBookmark | null>(null);

  previousState(): void {
    window.history.back();
  }
}
