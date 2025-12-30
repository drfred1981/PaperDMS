import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MetaBookmarkDetailComponent } from './meta-bookmark-detail.component';

describe('MetaBookmark Management Detail Component', () => {
  let comp: MetaBookmarkDetailComponent;
  let fixture: ComponentFixture<MetaBookmarkDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetaBookmarkDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./meta-bookmark-detail.component').then(m => m.MetaBookmarkDetailComponent),
              resolve: { metaBookmark: () => of({ id: 12947 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MetaBookmarkDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetaBookmarkDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load metaBookmark on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MetaBookmarkDetailComponent);

      // THEN
      expect(instance.metaBookmark()).toEqual(expect.objectContaining({ id: 12947 }));
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
