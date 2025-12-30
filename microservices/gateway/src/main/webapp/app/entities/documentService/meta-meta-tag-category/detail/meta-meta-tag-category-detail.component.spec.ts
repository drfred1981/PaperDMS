import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MetaMetaTagCategoryDetailComponent } from './meta-meta-tag-category-detail.component';

describe('MetaMetaTagCategory Management Detail Component', () => {
  let comp: MetaMetaTagCategoryDetailComponent;
  let fixture: ComponentFixture<MetaMetaTagCategoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetaMetaTagCategoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./meta-meta-tag-category-detail.component').then(m => m.MetaMetaTagCategoryDetailComponent),
              resolve: { metaMetaTagCategory: () => of({ id: 7139 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MetaMetaTagCategoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetaMetaTagCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load metaMetaTagCategory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MetaMetaTagCategoryDetailComponent);

      // THEN
      expect(instance.metaMetaTagCategory()).toEqual(expect.objectContaining({ id: 7139 }));
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
