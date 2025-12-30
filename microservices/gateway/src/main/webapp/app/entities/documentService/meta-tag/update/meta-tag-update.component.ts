import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMetaMetaTagCategory } from 'app/entities/documentService/meta-meta-tag-category/meta-meta-tag-category.model';
import { MetaMetaTagCategoryService } from 'app/entities/documentService/meta-meta-tag-category/service/meta-meta-tag-category.service';
import { IMetaTag } from '../meta-tag.model';
import { MetaTagService } from '../service/meta-tag.service';
import { MetaTagFormGroup, MetaTagFormService } from './meta-tag-form.service';

@Component({
  selector: 'jhi-meta-tag-update',
  templateUrl: './meta-tag-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaTagUpdateComponent implements OnInit {
  isSaving = false;
  metaTag: IMetaTag | null = null;

  metaMetaTagCategoriesSharedCollection: IMetaMetaTagCategory[] = [];

  protected metaTagService = inject(MetaTagService);
  protected metaTagFormService = inject(MetaTagFormService);
  protected metaMetaTagCategoryService = inject(MetaMetaTagCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaTagFormGroup = this.metaTagFormService.createMetaTagFormGroup();

  compareMetaMetaTagCategory = (o1: IMetaMetaTagCategory | null, o2: IMetaMetaTagCategory | null): boolean =>
    this.metaMetaTagCategoryService.compareMetaMetaTagCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaTag }) => {
      this.metaTag = metaTag;
      if (metaTag) {
        this.updateForm(metaTag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metaTag = this.metaTagFormService.getMetaTag(this.editForm);
    if (metaTag.id !== null) {
      this.subscribeToSaveResponse(this.metaTagService.update(metaTag));
    } else {
      this.subscribeToSaveResponse(this.metaTagService.create(metaTag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaTag>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(metaTag: IMetaTag): void {
    this.metaTag = metaTag;
    this.metaTagFormService.resetForm(this.editForm, metaTag);

    this.metaMetaTagCategoriesSharedCollection =
      this.metaMetaTagCategoryService.addMetaMetaTagCategoryToCollectionIfMissing<IMetaMetaTagCategory>(
        this.metaMetaTagCategoriesSharedCollection,
        metaTag.metaMetaTagCategory,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.metaMetaTagCategoryService
      .query()
      .pipe(map((res: HttpResponse<IMetaMetaTagCategory[]>) => res.body ?? []))
      .pipe(
        map((metaMetaTagCategories: IMetaMetaTagCategory[]) =>
          this.metaMetaTagCategoryService.addMetaMetaTagCategoryToCollectionIfMissing<IMetaMetaTagCategory>(
            metaMetaTagCategories,
            this.metaTag?.metaMetaTagCategory,
          ),
        ),
      )
      .subscribe((metaMetaTagCategories: IMetaMetaTagCategory[]) => (this.metaMetaTagCategoriesSharedCollection = metaMetaTagCategories));
  }
}
