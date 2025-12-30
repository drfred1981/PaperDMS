import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ImageConversionConfigDetailComponent } from './image-conversion-config-detail.component';

describe('ImageConversionConfig Management Detail Component', () => {
  let comp: ImageConversionConfigDetailComponent;
  let fixture: ComponentFixture<ImageConversionConfigDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageConversionConfigDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./image-conversion-config-detail.component').then(m => m.ImageConversionConfigDetailComponent),
              resolve: { imageConversionConfig: () => of({ id: 9973 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ImageConversionConfigDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageConversionConfigDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load imageConversionConfig on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImageConversionConfigDetailComponent);

      // THEN
      expect(instance.imageConversionConfig()).toEqual(expect.objectContaining({ id: 9973 }));
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
