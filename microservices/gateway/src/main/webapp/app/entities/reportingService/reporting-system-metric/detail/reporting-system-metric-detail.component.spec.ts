import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportingSystemMetricDetailComponent } from './reporting-system-metric-detail.component';

describe('ReportingSystemMetric Management Detail Component', () => {
  let comp: ReportingSystemMetricDetailComponent;
  let fixture: ComponentFixture<ReportingSystemMetricDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportingSystemMetricDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./reporting-system-metric-detail.component').then(m => m.ReportingSystemMetricDetailComponent),
              resolve: { reportingSystemMetric: () => of({ id: 23948 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportingSystemMetricDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportingSystemMetricDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load reportingSystemMetric on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportingSystemMetricDetailComponent);

      // THEN
      expect(instance.reportingSystemMetric()).toEqual(expect.objectContaining({ id: 23948 }));
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
