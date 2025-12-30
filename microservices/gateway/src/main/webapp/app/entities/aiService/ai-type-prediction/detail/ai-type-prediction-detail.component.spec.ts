import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AITypePredictionDetailComponent } from './ai-type-prediction-detail.component';

describe('AITypePrediction Management Detail Component', () => {
  let comp: AITypePredictionDetailComponent;
  let fixture: ComponentFixture<AITypePredictionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AITypePredictionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ai-type-prediction-detail.component').then(m => m.AITypePredictionDetailComponent),
              resolve: { aITypePrediction: () => of({ id: 133 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AITypePredictionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AITypePredictionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load aITypePrediction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AITypePredictionDetailComponent);

      // THEN
      expect(instance.aITypePrediction()).toEqual(expect.objectContaining({ id: 133 }));
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
