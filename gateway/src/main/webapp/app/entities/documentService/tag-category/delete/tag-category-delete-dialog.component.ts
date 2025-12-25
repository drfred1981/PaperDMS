import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITagCategory } from '../tag-category.model';
import { TagCategoryService } from '../service/tag-category.service';

@Component({
  templateUrl: './tag-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TagCategoryDeleteDialogComponent {
  tagCategory?: ITagCategory;

  protected tagCategoryService = inject(TagCategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
