import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITagCategory } from '../tag-category.model';

@Component({
  selector: 'jhi-tag-category-detail',
  templateUrl: './tag-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TagCategoryDetailComponent {
  tagCategory = input<ITagCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
