import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PerformanceMetricDetail } from './performance-metric-detail';

describe('PerformanceMetric Management Detail Component', () => {
  let comp: PerformanceMetricDetail;
  let fixture: ComponentFixture<PerformanceMetricDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./performance-metric-detail').then(m => m.PerformanceMetricDetail),
              resolve: { performanceMetric: () => of({ id: 7414 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformanceMetricDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load performanceMetric on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PerformanceMetricDetail);

      // THEN
      expect(instance.performanceMetric()).toEqual(expect.objectContaining({ id: 7414 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
