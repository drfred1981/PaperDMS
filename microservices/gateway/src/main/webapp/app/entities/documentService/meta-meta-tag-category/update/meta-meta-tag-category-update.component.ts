import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMetaMetaTagCategory } from '../meta-meta-tag-category.model';
import { MetaMetaTagCategoryService } from '../service/meta-meta-tag-category.service';
import { MetaMetaTagCategoryFormGroup, MetaMetaTagCategoryFormService } from './meta-meta-tag-category-form.service';

@Component({
  selector: 'jhi-meta-meta-tag-category-update',
  templateUrl: './meta-meta-tag-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaMetaTagCategoryUpdateComponent implements OnInit {
  isSaving = false;
  metaMetaTagCategory: IMetaMetaTagCategory | null = null;

  metaMetaTagCategoriesSharedCollection: IMetaMetaTagCategory[] = [];

  protected metaMetaTagCategoryService = inject(MetaMetaTagCategoryService);
  protected metaMetaTagCategoryFormService = inject(MetaMetaTagCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaMetaTagCategoryFormGroup = this.metaMetaTagCategoryFormService.createMetaMetaTagCategoryFormGroup();

  compareMetaMetaTagCategory = (o1: IMetaMetaTagCategory | null, o2: IMetaMetaTagCategory | null): boolean =>
    this.metaMetaTagCategoryService.compareMetaMetaTagCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaMetaTagCategory }) => {
      this.metaMetaTagCategory = metaMetaTagCategory;
      if (metaMetaTagCategory) {
        this.updateForm(metaMetaTagCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metaMetaTagCategory = this.metaMetaTagCategoryFormService.getMetaMetaTagCategory(this.editForm);
    if (metaMetaTagCategory.id !== null) {
      this.subscribeToSaveResponse(this.metaMetaTagCategoryService.update(metaMetaTagCategory));
    } else {
      this.subscribeToSaveResponse(this.metaMetaTagCategoryService.create(metaMetaTagCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaMetaTagCategory>>): void {
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

  protected updateForm(metaMetaTagCategory: IMetaMetaTagCategory): void {
    this.metaMetaTagCategory = metaMetaTagCategory;
    this.metaMetaTagCategoryFormService.resetForm(this.editForm, metaMetaTagCategory);

    this.metaMetaTagCategoriesSharedCollection =
      this.metaMetaTagCategoryService.addMetaMetaTagCategoryToCollectionIfMissing<IMetaMetaTagCategory>(
        this.metaMetaTagCategoriesSharedCollection,
        metaMetaTagCategory.parent,
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
            this.metaMetaTagCategory?.parent,
          ),
        ),
      )
      .subscribe((metaMetaTagCategories: IMetaMetaTagCategory[]) => (this.metaMetaTagCategoriesSharedCollection = metaMetaTagCategories));
  }
}
