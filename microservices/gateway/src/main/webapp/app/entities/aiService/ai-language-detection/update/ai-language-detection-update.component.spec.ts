import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AILanguageDetectionService } from '../service/ai-language-detection.service';
import { IAILanguageDetection } from '../ai-language-detection.model';
import { AILanguageDetectionFormService } from './ai-language-detection-form.service';

import { AILanguageDetectionUpdateComponent } from './ai-language-detection-update.component';

describe('AILanguageDetection Management Update Component', () => {
  let comp: AILanguageDetectionUpdateComponent;
  let fixture: ComponentFixture<AILanguageDetectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aILanguageDetectionFormService: AILanguageDetectionFormService;
  let aILanguageDetectionService: AILanguageDetectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AILanguageDetectionUpdateComponent],
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
      .overrideTemplate(AILanguageDetectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AILanguageDetectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aILanguageDetectionFormService = TestBed.inject(AILanguageDetectionFormService);
    aILanguageDetectionService = TestBed.inject(AILanguageDetectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const aILanguageDetection: IAILanguageDetection = { id: 26399 };

      activatedRoute.data = of({ aILanguageDetection });
      comp.ngOnInit();

      expect(comp.aILanguageDetection).toEqual(aILanguageDetection);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAILanguageDetection>>();
      const aILanguageDetection = { id: 3507 };
      jest.spyOn(aILanguageDetectionFormService, 'getAILanguageDetection').mockReturnValue(aILanguageDetection);
      jest.spyOn(aILanguageDetectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aILanguageDetection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aILanguageDetection }));
      saveSubject.complete();

      // THEN
      expect(aILanguageDetectionFormService.getAILanguageDetection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aILanguageDetectionService.update).toHaveBeenCalledWith(expect.objectContaining(aILanguageDetection));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAILanguageDetection>>();
      const aILanguageDetection = { id: 3507 };
      jest.spyOn(aILanguageDetectionFormService, 'getAILanguageDetection').mockReturnValue({ id: null });
      jest.spyOn(aILanguageDetectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aILanguageDetection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aILanguageDetection }));
      saveSubject.complete();

      // THEN
      expect(aILanguageDetectionFormService.getAILanguageDetection).toHaveBeenCalled();
      expect(aILanguageDetectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAILanguageDetection>>();
      const aILanguageDetection = { id: 3507 };
      jest.spyOn(aILanguageDetectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aILanguageDetection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aILanguageDetectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
