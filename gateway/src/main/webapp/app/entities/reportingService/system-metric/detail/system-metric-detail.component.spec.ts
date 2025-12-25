import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SystemMetricDetailComponent } from './system-metric-detail.component';

describe('SystemMetric Management Detail Component', () => {
  let comp: SystemMetricDetailComponent;
  let fixture: ComponentFixture<SystemMetricDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SystemMetricDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./system-metric-detail.component').then(m => m.SystemMetricDetailComponent),
              resolve: { systemMetric: () => of({ id: 14068 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SystemMetricDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemMetricDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load systemMetric on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SystemMetricDetailComponent);

      // THEN
      expect(instance.systemMetric()).toEqual(expect.objectContaining({ id: 14068 }));
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
