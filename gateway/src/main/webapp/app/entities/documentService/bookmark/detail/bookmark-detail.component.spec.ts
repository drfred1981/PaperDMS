import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BookmarkDetailComponent } from './bookmark-detail.component';

describe('Bookmark Management Detail Component', () => {
  let comp: BookmarkDetailComponent;
  let fixture: ComponentFixture<BookmarkDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookmarkDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bookmark-detail.component').then(m => m.BookmarkDetailComponent),
              resolve: { bookmark: () => of({ id: 18281 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BookmarkDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookmarkDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load bookmark on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BookmarkDetailComponent);

      // THEN
      expect(instance.bookmark()).toEqual(expect.objectContaining({ id: 18281 }));
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
