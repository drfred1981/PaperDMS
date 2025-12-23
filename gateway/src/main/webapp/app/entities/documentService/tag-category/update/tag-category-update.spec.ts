import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TagCategoryService } from '../service/tag-category.service';
import { ITagCategory } from '../tag-category.model';

import { TagCategoryFormService } from './tag-category-form.service';
import { TagCategoryUpdate } from './tag-category-update';

describe('TagCategory Management Update Component', () => {
  let comp: TagCategoryUpdate;
  let fixture: ComponentFixture<TagCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let tagCategoryFormService: TagCategoryFormService;
  let tagCategoryService: TagCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(TagCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagCategoryFormService = TestBed.inject(TagCategoryFormService);
    tagCategoryService = TestBed.inject(TagCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TagCategory query and add missing value', () => {
      const tagCategory: ITagCategory = { id: 13926 };
      const parent: ITagCategory = { id: 10601 };
      tagCategory.parent = parent;

      const tagCategoryCollection: ITagCategory[] = [{ id: 10601 }];
      jest.spyOn(tagCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCategoryCollection })));
      const additionalTagCategories = [parent];
      const expectedCollection: ITagCategory[] = [...additionalTagCategories, ...tagCategoryCollection];
      jest.spyOn(tagCategoryService, 'addTagCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tagCategory });
      comp.ngOnInit();

      expect(tagCategoryService.query).toHaveBeenCalled();
      expect(tagCategoryService.addTagCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        tagCategoryCollection,
        ...additionalTagCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.tagCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tagCategory: ITagCategory = { id: 13926 };
      const parent: ITagCategory = { id: 10601 };
      tagCategory.parent = parent;

      activatedRoute.data = of({ tagCategory });
      comp.ngOnInit();

      expect(comp.tagCategoriesSharedCollection()).toContainEqual(parent);
      expect(comp.tagCategory).toEqual(tagCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCategory>>();
      const tagCategory = { id: 10601 };
      jest.spyOn(tagCategoryFormService, 'getTagCategory').mockReturnValue(tagCategory);
      jest.spyOn(tagCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCategory }));
      saveSubject.complete();

      // THEN
      expect(tagCategoryFormService.getTagCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(tagCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCategory>>();
      const tagCategory = { id: 10601 };
      jest.spyOn(tagCategoryFormService, 'getTagCategory').mockReturnValue({ id: null });
      jest.spyOn(tagCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCategory }));
      saveSubject.complete();

      // THEN
      expect(tagCategoryFormService.getTagCategory).toHaveBeenCalled();
      expect(tagCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCategory>>();
      const tagCategory = { id: 10601 };
      jest.spyOn(tagCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTagCategory', () => {
      it('should forward to tagCategoryService', () => {
        const entity = { id: 10601 };
        const entity2 = { id: 13926 };
        jest.spyOn(tagCategoryService, 'compareTagCategory');
        comp.compareTagCategory(entity, entity2);
        expect(tagCategoryService.compareTagCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
