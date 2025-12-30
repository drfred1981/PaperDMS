import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaSavedSearchService } from '../service/meta-saved-search.service';
import { IMetaSavedSearch } from '../meta-saved-search.model';
import { MetaSavedSearchFormService } from './meta-saved-search-form.service';

import { MetaSavedSearchUpdateComponent } from './meta-saved-search-update.component';

describe('MetaSavedSearch Management Update Component', () => {
  let comp: MetaSavedSearchUpdateComponent;
  let fixture: ComponentFixture<MetaSavedSearchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaSavedSearchFormService: MetaSavedSearchFormService;
  let metaSavedSearchService: MetaSavedSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaSavedSearchUpdateComponent],
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
      .overrideTemplate(MetaSavedSearchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaSavedSearchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaSavedSearchFormService = TestBed.inject(MetaSavedSearchFormService);
    metaSavedSearchService = TestBed.inject(MetaSavedSearchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const metaSavedSearch: IMetaSavedSearch = { id: 26353 };

      activatedRoute.data = of({ metaSavedSearch });
      comp.ngOnInit();

      expect(comp.metaSavedSearch).toEqual(metaSavedSearch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSavedSearch>>();
      const metaSavedSearch = { id: 619 };
      jest.spyOn(metaSavedSearchFormService, 'getMetaSavedSearch').mockReturnValue(metaSavedSearch);
      jest.spyOn(metaSavedSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSavedSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaSavedSearch }));
      saveSubject.complete();

      // THEN
      expect(metaSavedSearchFormService.getMetaSavedSearch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaSavedSearchService.update).toHaveBeenCalledWith(expect.objectContaining(metaSavedSearch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSavedSearch>>();
      const metaSavedSearch = { id: 619 };
      jest.spyOn(metaSavedSearchFormService, 'getMetaSavedSearch').mockReturnValue({ id: null });
      jest.spyOn(metaSavedSearchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSavedSearch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaSavedSearch }));
      saveSubject.complete();

      // THEN
      expect(metaSavedSearchFormService.getMetaSavedSearch).toHaveBeenCalled();
      expect(metaSavedSearchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSavedSearch>>();
      const metaSavedSearch = { id: 619 };
      jest.spyOn(metaSavedSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSavedSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaSavedSearchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
