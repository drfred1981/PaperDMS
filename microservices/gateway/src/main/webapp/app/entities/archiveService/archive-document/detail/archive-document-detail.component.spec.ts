import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArchiveDocumentDetailComponent } from './archive-document-detail.component';

describe('ArchiveDocument Management Detail Component', () => {
  let comp: ArchiveDocumentDetailComponent;
  let fixture: ComponentFixture<ArchiveDocumentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArchiveDocumentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./archive-document-detail.component').then(m => m.ArchiveDocumentDetailComponent),
              resolve: { archiveDocument: () => of({ id: 30053 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ArchiveDocumentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchiveDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load archiveDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ArchiveDocumentDetailComponent);

      // THEN
      expect(instance.archiveDocument()).toEqual(expect.objectContaining({ id: 30053 }));
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
