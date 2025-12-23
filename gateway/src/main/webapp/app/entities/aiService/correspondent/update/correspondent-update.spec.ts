import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICorrespondentExtraction } from 'app/entities/aiService/correspondent-extraction/correspondent-extraction.model';
import { CorrespondentExtractionService } from 'app/entities/aiService/correspondent-extraction/service/correspondent-extraction.service';
import { ICorrespondent } from '../correspondent.model';
import { CorrespondentService } from '../service/correspondent.service';

import { CorrespondentFormService } from './correspondent-form.service';
import { CorrespondentUpdate } from './correspondent-update';

describe('Correspondent Management Update Component', () => {
  let comp: CorrespondentUpdate;
  let fixture: ComponentFixture<CorrespondentUpdate>;
  let activatedRoute: ActivatedRoute;
  let correspondentFormService: CorrespondentFormService;
  let correspondentService: CorrespondentService;
  let correspondentExtractionService: CorrespondentExtractionService;

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

    fixture = TestBed.createComponent(CorrespondentUpdate);
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
        ...additionalCorrespondentExtractions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.correspondentExtractionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const correspondent: ICorrespondent = { id: 12790 };
      const extraction: ICorrespondentExtraction = { id: 25332 };
      correspondent.extraction = extraction;

      activatedRoute.data = of({ correspondent });
      comp.ngOnInit();

      expect(comp.correspondentExtractionsSharedCollection()).toContainEqual(extraction);
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
