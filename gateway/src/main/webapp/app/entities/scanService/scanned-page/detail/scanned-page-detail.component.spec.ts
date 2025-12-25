import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ScannedPageDetailComponent } from './scanned-page-detail.component';

describe('ScannedPage Management Detail Component', () => {
  let comp: ScannedPageDetailComponent;
  let fixture: ComponentFixture<ScannedPageDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScannedPageDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./scanned-page-detail.component').then(m => m.ScannedPageDetailComponent),
              resolve: { scannedPage: () => of({ id: 4792 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ScannedPageDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScannedPageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load scannedPage on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ScannedPageDetailComponent);

      // THEN
      expect(instance.scannedPage()).toEqual(expect.objectContaining({ id: 4792 }));
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
