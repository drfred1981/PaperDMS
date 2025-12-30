import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMetaTag } from '../meta-tag.model';
import { MetaTagService } from '../service/meta-tag.service';

@Component({
  templateUrl: './meta-tag-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MetaTagDeleteDialogComponent {
  metaTag?: IMetaTag;

  protected metaTagService = inject(MetaTagService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.metaTagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
