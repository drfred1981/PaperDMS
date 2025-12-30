import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MonitoringSystemHealthDetailComponent } from './monitoring-system-health-detail.component';

describe('MonitoringSystemHealth Management Detail Component', () => {
  let comp: MonitoringSystemHealthDetailComponent;
  let fixture: ComponentFixture<MonitoringSystemHealthDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonitoringSystemHealthDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./monitoring-system-health-detail.component').then(m => m.MonitoringSystemHealthDetailComponent),
              resolve: { monitoringSystemHealth: () => of({ id: 14622 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MonitoringSystemHealthDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringSystemHealthDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load monitoringSystemHealth on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MonitoringSystemHealthDetailComponent);

      // THEN
      expect(instance.monitoringSystemHealth()).toEqual(expect.objectContaining({ id: 14622 }));
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
