import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SearchQueryService } from '../service/search-query.service';
import { ISearchQuery } from '../search-query.model';
import { SearchQueryFormService } from './search-query-form.service';

import { SearchQueryUpdateComponent } from './search-query-update.component';

describe('SearchQuery Management Update Component', () => {
  let comp: SearchQueryUpdateComponent;
  let fixture: ComponentFixture<SearchQueryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let searchQueryFormService: SearchQueryFormService;
  let searchQueryService: SearchQueryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SearchQueryUpdateComponent],
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
      .overrideTemplate(SearchQueryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SearchQueryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    searchQueryFormService = TestBed.inject(SearchQueryFormService);
    searchQueryService = TestBed.inject(SearchQueryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const searchQuery: ISearchQuery = { id: 23738 };

      activatedRoute.data = of({ searchQuery });
      comp.ngOnInit();

      expect(comp.searchQuery).toEqual(searchQuery);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchQuery>>();
      const searchQuery = { id: 14684 };
      jest.spyOn(searchQueryFormService, 'getSearchQuery').mockReturnValue(searchQuery);
      jest.spyOn(searchQueryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchQuery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchQuery }));
      saveSubject.complete();

      // THEN
      expect(searchQueryFormService.getSearchQuery).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(searchQueryService.update).toHaveBeenCalledWith(expect.objectContaining(searchQuery));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchQuery>>();
      const searchQuery = { id: 14684 };
      jest.spyOn(searchQueryFormService, 'getSearchQuery').mockReturnValue({ id: null });
      jest.spyOn(searchQueryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchQuery: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchQuery }));
      saveSubject.complete();

      // THEN
      expect(searchQueryFormService.getSearchQuery).toHaveBeenCalled();
      expect(searchQueryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchQuery>>();
      const searchQuery = { id: 14684 };
      jest.spyOn(searchQueryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchQuery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(searchQueryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
