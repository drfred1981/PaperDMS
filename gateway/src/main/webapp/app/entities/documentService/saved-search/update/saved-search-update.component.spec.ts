import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SavedSearchService } from '../service/saved-search.service';
import { ISavedSearch } from '../saved-search.model';
import { SavedSearchFormService } from './saved-search-form.service';

import { SavedSearchUpdateComponent } from './saved-search-update.component';

describe('SavedSearch Management Update Component', () => {
  let comp: SavedSearchUpdateComponent;
  let fixture: ComponentFixture<SavedSearchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let savedSearchFormService: SavedSearchFormService;
  let savedSearchService: SavedSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SavedSearchUpdateComponent],
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
      .overrideTemplate(SavedSearchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SavedSearchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    savedSearchFormService = TestBed.inject(SavedSearchFormService);
    savedSearchService = TestBed.inject(SavedSearchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const savedSearch: ISavedSearch = { id: 10324 };

      activatedRoute.data = of({ savedSearch });
      comp.ngOnInit();

      expect(comp.savedSearch).toEqual(savedSearch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISavedSearch>>();
      const savedSearch = { id: 32351 };
      jest.spyOn(savedSearchFormService, 'getSavedSearch').mockReturnValue(savedSearch);
      jest.spyOn(savedSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ savedSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: savedSearch }));
      saveSubject.complete();

      // THEN
      expect(savedSearchFormService.getSavedSearch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(savedSearchService.update).toHaveBeenCalledWith(expect.objectContaining(savedSearch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISavedSearch>>();
      const savedSearch = { id: 32351 };
      jest.spyOn(savedSearchFormService, 'getSavedSearch').mockReturnValue({ id: null });
      jest.spyOn(savedSearchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ savedSearch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: savedSearch }));
      saveSubject.complete();

      // THEN
      expect(savedSearchFormService.getSavedSearch).toHaveBeenCalled();
      expect(savedSearchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISavedSearch>>();
      const savedSearch = { id: 32351 };
      jest.spyOn(savedSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ savedSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(savedSearchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
