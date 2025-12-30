import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ImageConversionStatisticsDetailComponent } from './image-conversion-statistics-detail.component';

describe('ImageConversionStatistics Management Detail Component', () => {
  let comp: ImageConversionStatisticsDetailComponent;
  let fixture: ComponentFixture<ImageConversionStatisticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageConversionStatisticsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./image-conversion-statistics-detail.component').then(m => m.ImageConversionStatisticsDetailComponent),
              resolve: { imageConversionStatistics: () => of({ id: 23721 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ImageConversionStatisticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageConversionStatisticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load imageConversionStatistics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImageConversionStatisticsDetailComponent);

      // THEN
      expect(instance.imageConversionStatistics()).toEqual(expect.objectContaining({ id: 23721 }));
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
