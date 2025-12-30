import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AITagPredictionDetailComponent } from './ai-tag-prediction-detail.component';

describe('AITagPrediction Management Detail Component', () => {
  let comp: AITagPredictionDetailComponent;
  let fixture: ComponentFixture<AITagPredictionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AITagPredictionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ai-tag-prediction-detail.component').then(m => m.AITagPredictionDetailComponent),
              resolve: { aITagPrediction: () => of({ id: 13214 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AITagPredictionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AITagPredictionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load aITagPrediction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AITagPredictionDetailComponent);

      // THEN
      expect(instance.aITagPrediction()).toEqual(expect.objectContaining({ id: 13214 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
