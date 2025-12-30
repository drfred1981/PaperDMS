import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ImageConversionBatchService } from '../service/image-conversion-batch.service';
import { IImageConversionBatch } from '../image-conversion-batch.model';
import { ImageConversionBatchFormService } from './image-conversion-batch-form.service';

import { ImageConversionBatchUpdateComponent } from './image-conversion-batch-update.component';

describe('ImageConversionBatch Management Update Component', () => {
  let comp: ImageConversionBatchUpdateComponent;
  let fixture: ComponentFixture<ImageConversionBatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageConversionBatchFormService: ImageConversionBatchFormService;
  let imageConversionBatchService: ImageConversionBatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageConversionBatchUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ImageConversionBatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageConversionBatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageConversionBatchFormService = TestBed.inject(ImageConversionBatchFormService);
    imageConversionBatchService = TestBed.inject(ImageConversionBatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const imageConversionBatch: IImageConversionBatch = { id: 12076 };

      activatedRoute.data = of({ imageConversionBatch });
      comp.ngOnInit();

      expect(comp.imageConversionBatch).toEqual(imageConversionBatch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionBatch>>();
      const imageConversionBatch = { id: 31426 };
      jest.spyOn(imageConversionBatchFormService, 'getImageConversionBatch').mockReturnValue(imageConversionBatch);
      jest.spyOn(imageConversionBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionBatch }));
      saveSubject.complete();

      // THEN
      expect(imageConversionBatchFormService.getImageConversionBatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageConversionBatchService.update).toHaveBeenCalledWith(expect.objectContaining(imageConversionBatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionBatch>>();
      const imageConversionBatch = { id: 31426 };
      jest.spyOn(imageConversionBatchFormService, 'getImageConversionBatch').mockReturnValue({ id: null });
      jest.spyOn(imageConversionBatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionBatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionBatch }));
      saveSubject.complete();

      // THEN
      expect(imageConversionBatchFormService.getImageConversionBatch).toHaveBeenCalled();
      expect(imageConversionBatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionBatch>>();
      const imageConversionBatch = { id: 31426 };
      jest.spyOn(imageConversionBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageConversionBatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
