import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DocumentTypeFieldDetailComponent } from './document-type-field-detail.component';

describe('DocumentTypeField Management Detail Component', () => {
  let comp: DocumentTypeFieldDetailComponent;
  let fixture: ComponentFixture<DocumentTypeFieldDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentTypeFieldDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./document-type-field-detail.component').then(m => m.DocumentTypeFieldDetailComponent),
              resolve: { documentTypeField: () => of({ id: 31753 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DocumentTypeFieldDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentTypeFieldDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load documentTypeField on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentTypeFieldDetailComponent);

      // THEN
      expect(instance.documentTypeField()).toEqual(expect.objectContaining({ id: 31753 }));
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
