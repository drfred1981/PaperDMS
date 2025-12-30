import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SearchIndexService } from '../service/search-index.service';
import { ISearchIndex } from '../search-index.model';
import { SearchIndexFormService } from './search-index-form.service';

import { SearchIndexUpdateComponent } from './search-index-update.component';

describe('SearchIndex Management Update Component', () => {
  let comp: SearchIndexUpdateComponent;
  let fixture: ComponentFixture<SearchIndexUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let searchIndexFormService: SearchIndexFormService;
  let searchIndexService: SearchIndexService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SearchIndexUpdateComponent],
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
      .overrideTemplate(SearchIndexUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SearchIndexUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    searchIndexFormService = TestBed.inject(SearchIndexFormService);
    searchIndexService = TestBed.inject(SearchIndexService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const searchIndex: ISearchIndex = { id: 27227 };

      activatedRoute.data = of({ searchIndex });
      comp.ngOnInit();

      expect(comp.searchIndex).toEqual(searchIndex);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchIndex>>();
      const searchIndex = { id: 18426 };
      jest.spyOn(searchIndexFormService, 'getSearchIndex').mockReturnValue(searchIndex);
      jest.spyOn(searchIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchIndex }));
      saveSubject.complete();

      // THEN
      expect(searchIndexFormService.getSearchIndex).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(searchIndexService.update).toHaveBeenCalledWith(expect.objectContaining(searchIndex));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchIndex>>();
      const searchIndex = { id: 18426 };
      jest.spyOn(searchIndexFormService, 'getSearchIndex').mockReturnValue({ id: null });
      jest.spyOn(searchIndexService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchIndex: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchIndex }));
      saveSubject.complete();

      // THEN
      expect(searchIndexFormService.getSearchIndex).toHaveBeenCalled();
      expect(searchIndexService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchIndex>>();
      const searchIndex = { id: 18426 };
      jest.spyOn(searchIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(searchIndexService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
