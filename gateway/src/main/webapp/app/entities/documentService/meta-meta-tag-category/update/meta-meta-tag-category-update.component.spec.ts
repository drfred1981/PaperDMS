import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaMetaTagCategoryService } from '../service/meta-meta-tag-category.service';
import { IMetaMetaTagCategory } from '../meta-meta-tag-category.model';
import { MetaMetaTagCategoryFormService } from './meta-meta-tag-category-form.service';

import { MetaMetaTagCategoryUpdateComponent } from './meta-meta-tag-category-update.component';

describe('MetaMetaTagCategory Management Update Component', () => {
  let comp: MetaMetaTagCategoryUpdateComponent;
  let fixture: ComponentFixture<MetaMetaTagCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaMetaTagCategoryFormService: MetaMetaTagCategoryFormService;
  let metaMetaTagCategoryService: MetaMetaTagCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaMetaTagCategoryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MetaMetaTagCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaMetaTagCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaMetaTagCategoryFormService = TestBed.inject(MetaMetaTagCategoryFormService);
    metaMetaTagCategoryService = TestBed.inject(MetaMetaTagCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MetaMetaTagCategory query and add missing value', () => {
      const metaMetaTagCategory: IMetaMetaTagCategory = { id: 29219 };
      const parent: IMetaMetaTagCategory = { id: 7139 };
      metaMetaTagCategory.parent = parent;

      const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [{ id: 7139 }];
      jest.spyOn(metaMetaTagCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: metaMetaTagCategoryCollection })));
      const additionalMetaMetaTagCategories = [parent];
      const expectedCollection: IMetaMetaTagCategory[] = [...additionalMetaMetaTagCategories, ...metaMetaTagCategoryCollection];
      jest.spyOn(metaMetaTagCategoryService, 'addMetaMetaTagCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ metaMetaTagCategory });
      comp.ngOnInit();

      expect(metaMetaTagCategoryService.query).toHaveBeenCalled();
      expect(metaMetaTagCategoryService.addMetaMetaTagCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        metaMetaTagCategoryCollection,
        ...additionalMetaMetaTagCategories.map(expect.objectContaining),
      );
      expect(comp.metaMetaTagCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const metaMetaTagCategory: IMetaMetaTagCategory = { id: 29219 };
      const parent: IMetaMetaTagCategory = { id: 7139 };
      metaMetaTagCategory.parent = parent;

      activatedRoute.data = of({ metaMetaTagCategory });
      comp.ngOnInit();

      expect(comp.metaMetaTagCategoriesSharedCollection).toContainEqual(parent);
      expect(comp.metaMetaTagCategory).toEqual(metaMetaTagCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaMetaTagCategory>>();
      const metaMetaTagCategory = { id: 7139 };
      jest.spyOn(metaMetaTagCategoryFormService, 'getMetaMetaTagCategory').mockReturnValue(metaMetaTagCategory);
      jest.spyOn(metaMetaTagCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaMetaTagCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaMetaTagCategory }));
      saveSubject.complete();

      // THEN
      expect(metaMetaTagCategoryFormService.getMetaMetaTagCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaMetaTagCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(metaMetaTagCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaMetaTagCategory>>();
      const metaMetaTagCategory = { id: 7139 };
      jest.spyOn(metaMetaTagCategoryFormService, 'getMetaMetaTagCategory').mockReturnValue({ id: null });
      jest.spyOn(metaMetaTagCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaMetaTagCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaMetaTagCategory }));
      saveSubject.complete();

      // THEN
      expect(metaMetaTagCategoryFormService.getMetaMetaTagCategory).toHaveBeenCalled();
      expect(metaMetaTagCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaMetaTagCategory>>();
      const metaMetaTagCategory = { id: 7139 };
      jest.spyOn(metaMetaTagCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaMetaTagCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaMetaTagCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMetaMetaTagCategory', () => {
      it('should forward to metaMetaTagCategoryService', () => {
        const entity = { id: 7139 };
        const entity2 = { id: 29219 };
        jest.spyOn(metaMetaTagCategoryService, 'compareMetaMetaTagCategory');
        comp.compareMetaMetaTagCategory(entity, entity2);
        expect(metaMetaTagCategoryService.compareMetaMetaTagCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
