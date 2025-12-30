import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportingPerformanceMetricDetailComponent } from './reporting-performance-metric-detail.component';

describe('ReportingPerformanceMetric Management Detail Component', () => {
  let comp: ReportingPerformanceMetricDetailComponent;
  let fixture: ComponentFixture<ReportingPerformanceMetricDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportingPerformanceMetricDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./reporting-performance-metric-detail.component').then(m => m.ReportingPerformanceMetricDetailComponent),
              resolve: { reportingPerformanceMetric: () => of({ id: 32088 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportingPerformanceMetricDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportingPerformanceMetricDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load reportingPerformanceMetric on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportingPerformanceMetricDetailComponent);

      // THEN
      expect(instance.reportingPerformanceMetric()).toEqual(expect.objectContaining({ id: 32088 }));
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
