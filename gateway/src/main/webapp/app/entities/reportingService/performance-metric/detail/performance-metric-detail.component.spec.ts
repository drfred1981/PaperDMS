import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PerformanceMetricDetailComponent } from './performance-metric-detail.component';

describe('PerformanceMetric Management Detail Component', () => {
  let comp: PerformanceMetricDetailComponent;
  let fixture: ComponentFixture<PerformanceMetricDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerformanceMetricDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./performance-metric-detail.component').then(m => m.PerformanceMetricDetailComponent),
              resolve: { performanceMetric: () => of({ id: 7414 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PerformanceMetricDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformanceMetricDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load performanceMetric on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PerformanceMetricDetailComponent);

      // THEN
      expect(instance.performanceMetric()).toEqual(expect.objectContaining({ id: 7414 }));
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
