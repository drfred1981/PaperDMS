import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITagCategory } from 'app/entities/documentService/tag-category/tag-category.model';
import { TagCategoryService } from 'app/entities/documentService/tag-category/service/tag-category.service';
import { ITag } from '../tag.model';
import { TagService } from '../service/tag.service';
import { TagFormGroup, TagFormService } from './tag-form.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;
  tag: ITag | null = null;

  tagCategoriesSharedCollection: ITagCategory[] = [];

  protected tagService = inject(TagService);
  protected tagFormService = inject(TagFormService);
  protected tagCategoryService = inject(TagCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TagFormGroup = this.tagFormService.createTagFormGroup();

  compareTagCategory = (o1: ITagCategory | null, o2: ITagCategory | null): boolean => this.tagCategoryService.compareTagCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.tag = tag;
      if (tag) {
        this.updateForm(tag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.tagFormService.getTag(this.editForm);
    if (tag.id !== null) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  protected updateForm(tag: ITag): void {
    this.tag = tag;
    this.tagFormService.resetForm(this.editForm, tag);

    this.tagCategoriesSharedCollection = this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(
      this.tagCategoriesSharedCollection,
      tag.tagCategory,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITagCategory[]>) => res.body ?? []))
      .pipe(
        map((tagCategories: ITagCategory[]) =>
          this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(tagCategories, this.tag?.tagCategory),
        ),
      )
      .subscribe((tagCategories: ITagCategory[]) => (this.tagCategoriesSharedCollection = tagCategories));
  }
}
