import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ITagCategory } from '../tag-category.model';

@Component({
  selector: 'jhi-tag-category-detail',
  templateUrl: './tag-category-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class TagCategoryDetail {
  tagCategory = input<ITagCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
