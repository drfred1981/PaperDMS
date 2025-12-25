import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SemanticSearchService } from '../service/semantic-search.service';
import { ISemanticSearch } from '../semantic-search.model';
import { SemanticSearchFormService } from './semantic-search-form.service';

import { SemanticSearchUpdateComponent } from './semantic-search-update.component';

describe('SemanticSearch Management Update Component', () => {
  let comp: SemanticSearchUpdateComponent;
  let fixture: ComponentFixture<SemanticSearchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let semanticSearchFormService: SemanticSearchFormService;
  let semanticSearchService: SemanticSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SemanticSearchUpdateComponent],
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
      .overrideTemplate(SemanticSearchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SemanticSearchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    semanticSearchFormService = TestBed.inject(SemanticSearchFormService);
    semanticSearchService = TestBed.inject(SemanticSearchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const semanticSearch: ISemanticSearch = { id: 25967 };

      activatedRoute.data = of({ semanticSearch });
      comp.ngOnInit();

      expect(comp.semanticSearch).toEqual(semanticSearch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISemanticSearch>>();
      const semanticSearch = { id: 28291 };
      jest.spyOn(semanticSearchFormService, 'getSemanticSearch').mockReturnValue(semanticSearch);
      jest.spyOn(semanticSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semanticSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: semanticSearch }));
      saveSubject.complete();

      // THEN
      expect(semanticSearchFormService.getSemanticSearch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(semanticSearchService.update).toHaveBeenCalledWith(expect.objectContaining(semanticSearch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISemanticSearch>>();
      const semanticSearch = { id: 28291 };
      jest.spyOn(semanticSearchFormService, 'getSemanticSearch').mockReturnValue({ id: null });
      jest.spyOn(semanticSearchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semanticSearch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: semanticSearch }));
      saveSubject.complete();

      // THEN
      expect(semanticSearchFormService.getSemanticSearch).toHaveBeenCalled();
      expect(semanticSearchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISemanticSearch>>();
      const semanticSearch = { id: 28291 };
      jest.spyOn(semanticSearchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semanticSearch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(semanticSearchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
