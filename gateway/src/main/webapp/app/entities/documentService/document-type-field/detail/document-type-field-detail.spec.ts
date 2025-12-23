import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { TranslateModule } from '@ngx-translate/core';
import { of } from 'rxjs';

import { DocumentTypeFieldDetail } from './document-type-field-detail';

describe('DocumentTypeField Management Detail Component', () => {
  let comp: DocumentTypeFieldDetail;
  let fixture: ComponentFixture<DocumentTypeFieldDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./document-type-field-detail').then(m => m.DocumentTypeFieldDetail),
              resolve: { documentTypeField: () => of({ id: 31753 }) },
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
    fixture = TestBed.createComponent(DocumentTypeFieldDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load documentTypeField on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentTypeFieldDetail);

      // THEN
      expect(instance.documentTypeField()).toEqual(expect.objectContaining({ id: 31753 }));
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
