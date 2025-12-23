import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISearchIndex } from '../search-index.model';
import { SearchIndexService } from '../service/search-index.service';

import { SearchIndexFormService } from './search-index-form.service';
import { SearchIndexUpdate } from './search-index-update';

describe('SearchIndex Management Update Component', () => {
  let comp: SearchIndexUpdate;
  let fixture: ComponentFixture<SearchIndexUpdate>;
  let activatedRoute: ActivatedRoute;
  let searchIndexFormService: SearchIndexFormService;
  let searchIndexService: SearchIndexService;

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

    fixture = TestBed.createComponent(SearchIndexUpdate);
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
