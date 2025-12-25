import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TagPredictionDetailComponent } from './tag-prediction-detail.component';

describe('TagPrediction Management Detail Component', () => {
  let comp: TagPredictionDetailComponent;
  let fixture: ComponentFixture<TagPredictionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TagPredictionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tag-prediction-detail.component').then(m => m.TagPredictionDetailComponent),
              resolve: { tagPrediction: () => of({ id: 7335 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TagPredictionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TagPredictionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tagPrediction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TagPredictionDetailComponent);

      // THEN
      expect(instance.tagPrediction()).toEqual(expect.objectContaining({ id: 7335 }));
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
