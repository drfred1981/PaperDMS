import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ImageConversionBatchDetailComponent } from './image-conversion-batch-detail.component';

describe('ImageConversionBatch Management Detail Component', () => {
  let comp: ImageConversionBatchDetailComponent;
  let fixture: ComponentFixture<ImageConversionBatchDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageConversionBatchDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./image-conversion-batch-detail.component').then(m => m.ImageConversionBatchDetailComponent),
              resolve: { imageConversionBatch: () => of({ id: 31426 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ImageConversionBatchDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageConversionBatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load imageConversionBatch on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImageConversionBatchDetailComponent);

      // THEN
      expect(instance.imageConversionBatch()).toEqual(expect.objectContaining({ id: 31426 }));
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
