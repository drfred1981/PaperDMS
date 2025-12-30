import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AITypePredictionService } from '../service/ai-type-prediction.service';
import { IAITypePrediction } from '../ai-type-prediction.model';
import { AITypePredictionFormService } from './ai-type-prediction-form.service';

import { AITypePredictionUpdateComponent } from './ai-type-prediction-update.component';

describe('AITypePrediction Management Update Component', () => {
  let comp: AITypePredictionUpdateComponent;
  let fixture: ComponentFixture<AITypePredictionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aITypePredictionFormService: AITypePredictionFormService;
  let aITypePredictionService: AITypePredictionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AITypePredictionUpdateComponent],
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
      .overrideTemplate(AITypePredictionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AITypePredictionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aITypePredictionFormService = TestBed.inject(AITypePredictionFormService);
    aITypePredictionService = TestBed.inject(AITypePredictionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const aITypePrediction: IAITypePrediction = { id: 17407 };

      activatedRoute.data = of({ aITypePrediction });
      comp.ngOnInit();

      expect(comp.aITypePrediction).toEqual(aITypePrediction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITypePrediction>>();
      const aITypePrediction = { id: 133 };
      jest.spyOn(aITypePredictionFormService, 'getAITypePrediction').mockReturnValue(aITypePrediction);
      jest.spyOn(aITypePredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITypePrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aITypePrediction }));
      saveSubject.complete();

      // THEN
      expect(aITypePredictionFormService.getAITypePrediction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aITypePredictionService.update).toHaveBeenCalledWith(expect.objectContaining(aITypePrediction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITypePrediction>>();
      const aITypePrediction = { id: 133 };
      jest.spyOn(aITypePredictionFormService, 'getAITypePrediction').mockReturnValue({ id: null });
      jest.spyOn(aITypePredictionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITypePrediction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aITypePrediction }));
      saveSubject.complete();

      // THEN
      expect(aITypePredictionFormService.getAITypePrediction).toHaveBeenCalled();
      expect(aITypePredictionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITypePrediction>>();
      const aITypePrediction = { id: 133 };
      jest.spyOn(aITypePredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITypePrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aITypePredictionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
