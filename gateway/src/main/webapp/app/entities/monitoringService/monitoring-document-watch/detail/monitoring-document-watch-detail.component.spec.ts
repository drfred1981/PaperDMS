import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MonitoringDocumentWatchDetailComponent } from './monitoring-document-watch-detail.component';

describe('MonitoringDocumentWatch Management Detail Component', () => {
  let comp: MonitoringDocumentWatchDetailComponent;
  let fixture: ComponentFixture<MonitoringDocumentWatchDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonitoringDocumentWatchDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./monitoring-document-watch-detail.component').then(m => m.MonitoringDocumentWatchDetailComponent),
              resolve: { monitoringDocumentWatch: () => of({ id: 10460 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MonitoringDocumentWatchDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringDocumentWatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load monitoringDocumentWatch on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MonitoringDocumentWatchDetailComponent);

      // THEN
      expect(instance.monitoringDocumentWatch()).toEqual(expect.objectContaining({ id: 10460 }));
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
