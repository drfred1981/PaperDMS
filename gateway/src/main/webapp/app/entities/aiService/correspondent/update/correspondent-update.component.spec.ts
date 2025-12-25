import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICorrespondentExtraction } from 'app/entities/aiService/correspondent-extraction/correspondent-extraction.model';
import { CorrespondentExtractionService } from 'app/entities/aiService/correspondent-extraction/service/correspondent-extraction.service';
import { CorrespondentService } from '../service/correspondent.service';
import { ICorrespondent } from '../correspondent.model';
import { CorrespondentFormService } from './correspondent-form.service';

import { CorrespondentUpdateComponent } from './correspondent-update.component';

describe('Correspondent Management Update Component', () => {
  let comp: CorrespondentUpdateComponent;
  let fixture: ComponentFixture<CorrespondentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let correspondentFormService: CorrespondentFormService;
  let correspondentService: CorrespondentService;
  let correspondentExtractionService: CorrespondentExtractionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CorrespondentUpdateComponent],
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
      .overrideTemplate(CorrespondentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CorrespondentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    correspondentFormService = TestBed.inject(CorrespondentFormService);
    correspondentService = TestBed.inject(CorrespondentService);
    correspondentExtractionService = TestBed.inject(CorrespondentExtractionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CorrespondentExtraction query and add missing value', () => {
      const correspondent: ICorrespondent = { id: 12790 };
      const extraction: ICorrespondentExtraction = { id: 25332 };
      correspondent.extraction = extraction;

      const correspondentExtractionCollection: ICorrespondentExtraction[] = [{ id: 25332 }];
      jest
        .spyOn(correspondentExtractionService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: correspondentExtractionCollection })));
      const additionalCorrespondentExtractions = [extraction];
      const expectedCollection: ICorrespondentExtraction[] = [...additionalCorrespondentExtractions, ...correspondentExtractionCollection];
      jest.spyOn(correspondentExtractionService, 'addCorrespondentExtractionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ correspondent });
      comp.ngOnInit();

      expect(correspondentExtractionService.query).toHaveBeenCalled();
      expect(correspondentExtractionService.addCorrespondentExtractionToCollectionIfMissing).toHaveBeenCalledWith(
        correspondentExtractionCollection,
        ...additionalCorrespondentExtractions.map(expect.objectContaining),
      );
      expect(comp.correspondentExtractionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const correspondent: ICorrespondent = { id: 12790 };
      const extraction: ICorrespondentExtraction = { id: 25332 };
      correspondent.extraction = extraction;

      activatedRoute.data = of({ correspondent });
      comp.ngOnInit();

      expect(comp.correspondentExtractionsSharedCollection).toContainEqual(extraction);
      expect(comp.correspondent).toEqual(correspondent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondent>>();
      const correspondent = { id: 25663 };
      jest.spyOn(correspondentFormService, 'getCorrespondent').mockReturnValue(correspondent);
      jest.spyOn(correspondentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: correspondent }));
      saveSubject.complete();

      // THEN
      expect(correspondentFormService.getCorrespondent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(correspondentService.update).toHaveBeenCalledWith(expect.objectContaining(correspondent));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondent>>();
      const correspondent = { id: 25663 };
      jest.spyOn(correspondentFormService, 'getCorrespondent').mockReturnValue({ id: null });
      jest.spyOn(correspondentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: correspondent }));
      saveSubject.complete();

      // THEN
      expect(correspondentFormService.getCorrespondent).toHaveBeenCalled();
      expect(correspondentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondent>>();
      const correspondent = { id: 25663 };
      jest.spyOn(correspondentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(correspondentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCorrespondentExtraction', () => {
      it('should forward to correspondentExtractionService', () => {
        const entity = { id: 25332 };
        const entity2 = { id: 2844 };
        jest.spyOn(correspondentExtractionService, 'compareCorrespondentExtraction');
        comp.compareCorrespondentExtraction(entity, entity2);
        expect(correspondentExtractionService.compareCorrespondentExtraction).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
