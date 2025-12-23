import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ArchiveDocumentDetail } from './archive-document-detail';

describe('ArchiveDocument Management Detail Component', () => {
  let comp: ArchiveDocumentDetail;
  let fixture: ComponentFixture<ArchiveDocumentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./archive-document-detail').then(m => m.ArchiveDocumentDetail),
              resolve: { archiveDocument: () => of({ id: 30053 }) },
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
    fixture = TestBed.createComponent(ArchiveDocumentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load archiveDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ArchiveDocumentDetail);

      // THEN
      expect(instance.archiveDocument()).toEqual(expect.objectContaining({ id: 30053 }));
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
