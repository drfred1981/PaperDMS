import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MonitoringServiceStatusDetailComponent } from './monitoring-service-status-detail.component';

describe('MonitoringServiceStatus Management Detail Component', () => {
  let comp: MonitoringServiceStatusDetailComponent;
  let fixture: ComponentFixture<MonitoringServiceStatusDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonitoringServiceStatusDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./monitoring-service-status-detail.component').then(m => m.MonitoringServiceStatusDetailComponent),
              resolve: { monitoringServiceStatus: () => of({ id: 22154 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MonitoringServiceStatusDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringServiceStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load monitoringServiceStatus on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MonitoringServiceStatusDetailComponent);

      // THEN
      expect(instance.monitoringServiceStatus()).toEqual(expect.objectContaining({ id: 22154 }));
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
