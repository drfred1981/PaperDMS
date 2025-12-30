import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISimilarityCluster } from '../similarity-cluster.model';
import { SimilarityClusterService } from '../service/similarity-cluster.service';

@Component({
  templateUrl: './similarity-cluster-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SimilarityClusterDeleteDialogComponent {
  similarityCluster?: ISimilarityCluster;

  protected similarityClusterService = inject(SimilarityClusterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.similarityClusterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
