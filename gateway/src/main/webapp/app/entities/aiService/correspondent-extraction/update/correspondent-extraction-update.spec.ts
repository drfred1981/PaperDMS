import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICorrespondentExtraction } from '../correspondent-extraction.model';
import { CorrespondentExtractionService } from '../service/correspondent-extraction.service';

import { CorrespondentExtractionFormService } from './correspondent-extraction-form.service';
import { CorrespondentExtractionUpdate } from './correspondent-extraction-update';

describe('CorrespondentExtraction Management Update Component', () => {
  let comp: CorrespondentExtractionUpdate;
  let fixture: ComponentFixture<CorrespondentExtractionUpdate>;
  let activatedRoute: ActivatedRoute;
  let correspondentExtractionFormService: CorrespondentExtractionFormService;
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

    fixture = TestBed.createComponent(CorrespondentExtractionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    correspondentExtractionFormService = TestBed.inject(CorrespondentExtractionFormService);
    correspondentExtractionService = TestBed.inject(CorrespondentExtractionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const correspondentExtraction: ICorrespondentExtraction = { id: 2844 };

      activatedRoute.data = of({ correspondentExtraction });
      comp.ngOnInit();

      expect(comp.correspondentExtraction).toEqual(correspondentExtraction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondentExtraction>>();
      const correspondentExtraction = { id: 25332 };
      jest.spyOn(correspondentExtractionFormService, 'getCorrespondentExtraction').mockReturnValue(correspondentExtraction);
      jest.spyOn(correspondentExtractionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondentExtraction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: correspondentExtraction }));
      saveSubject.complete();

      // THEN
      expect(correspondentExtractionFormService.getCorrespondentExtraction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(correspondentExtractionService.update).toHaveBeenCalledWith(expect.objectContaining(correspondentExtraction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondentExtraction>>();
      const correspondentExtraction = { id: 25332 };
      jest.spyOn(correspondentExtractionFormService, 'getCorrespondentExtraction').mockReturnValue({ id: null });
      jest.spyOn(correspondentExtractionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondentExtraction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: correspondentExtraction }));
      saveSubject.complete();

      // THEN
      expect(correspondentExtractionFormService.getCorrespondentExtraction).toHaveBeenCalled();
      expect(correspondentExtractionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorrespondentExtraction>>();
      const correspondentExtraction = { id: 25332 };
      jest.spyOn(correspondentExtractionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correspondentExtraction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(correspondentExtractionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
