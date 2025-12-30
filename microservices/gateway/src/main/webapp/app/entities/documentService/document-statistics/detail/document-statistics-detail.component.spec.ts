import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DocumentStatisticsDetailComponent } from './document-statistics-detail.component';

describe('DocumentStatistics Management Detail Component', () => {
  let comp: DocumentStatisticsDetailComponent;
  let fixture: ComponentFixture<DocumentStatisticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentStatisticsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./document-statistics-detail.component').then(m => m.DocumentStatisticsDetailComponent),
              resolve: { documentStatistics: () => of({ id: 5208 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DocumentStatisticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentStatisticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load documentStatistics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentStatisticsDetailComponent);

      // THEN
      expect(instance.documentStatistics()).toEqual(expect.objectContaining({ id: 5208 }));
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
