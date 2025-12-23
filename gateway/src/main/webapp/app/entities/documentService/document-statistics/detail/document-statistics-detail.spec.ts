import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { DocumentStatisticsDetail } from './document-statistics-detail';

describe('DocumentStatistics Management Detail Component', () => {
  let comp: DocumentStatisticsDetail;
  let fixture: ComponentFixture<DocumentStatisticsDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./document-statistics-detail').then(m => m.DocumentStatisticsDetail),
              resolve: { documentStatistics: () => of({ id: 5208 }) },
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
    fixture = TestBed.createComponent(DocumentStatisticsDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load documentStatistics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentStatisticsDetail);

      // THEN
      expect(instance.documentStatistics()).toEqual(expect.objectContaining({ id: 5208 }));
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
