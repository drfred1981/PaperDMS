import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IImageConversionBatch } from 'app/entities/pdfToImageService/image-conversion-batch/image-conversion-batch.model';
import { ImageConversionBatchService } from 'app/entities/pdfToImageService/image-conversion-batch/service/image-conversion-batch.service';
import { ImagePdfConversionRequestService } from '../service/image-pdf-conversion-request.service';
import { IImagePdfConversionRequest } from '../image-pdf-conversion-request.model';
import { ImagePdfConversionRequestFormService } from './image-pdf-conversion-request-form.service';

import { ImagePdfConversionRequestUpdateComponent } from './image-pdf-conversion-request-update.component';

describe('ImagePdfConversionRequest Management Update Component', () => {
  let comp: ImagePdfConversionRequestUpdateComponent;
  let fixture: ComponentFixture<ImagePdfConversionRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imagePdfConversionRequestFormService: ImagePdfConversionRequestFormService;
  let imagePdfConversionRequestService: ImagePdfConversionRequestService;
  let imageConversionBatchService: ImageConversionBatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImagePdfConversionRequestUpdateComponent],
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
      .overrideTemplate(ImagePdfConversionRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagePdfConversionRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imagePdfConversionRequestFormService = TestBed.inject(ImagePdfConversionRequestFormService);
    imagePdfConversionRequestService = TestBed.inject(ImagePdfConversionRequestService);
    imageConversionBatchService = TestBed.inject(ImageConversionBatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ImageConversionBatch query and add missing value', () => {
      const imagePdfConversionRequest: IImagePdfConversionRequest = { id: 32495 };
      const batch: IImageConversionBatch = { id: 31426 };
      imagePdfConversionRequest.batch = batch;

      const imageConversionBatchCollection: IImageConversionBatch[] = [{ id: 31426 }];
      jest.spyOn(imageConversionBatchService, 'query').mockReturnValue(of(new HttpResponse({ body: imageConversionBatchCollection })));
      const additionalImageConversionBatches = [batch];
      const expectedCollection: IImageConversionBatch[] = [...additionalImageConversionBatches, ...imageConversionBatchCollection];
      jest.spyOn(imageConversionBatchService, 'addImageConversionBatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imagePdfConversionRequest });
      comp.ngOnInit();

      expect(imageConversionBatchService.query).toHaveBeenCalled();
      expect(imageConversionBatchService.addImageConversionBatchToCollectionIfMissing).toHaveBeenCalledWith(
        imageConversionBatchCollection,
        ...additionalImageConversionBatches.map(expect.objectContaining),
      );
      expect(comp.imageConversionBatchesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const imagePdfConversionRequest: IImagePdfConversionRequest = { id: 32495 };
      const batch: IImageConversionBatch = { id: 31426 };
      imagePdfConversionRequest.batch = batch;

      activatedRoute.data = of({ imagePdfConversionRequest });
      comp.ngOnInit();

      expect(comp.imageConversionBatchesSharedCollection).toContainEqual(batch);
      expect(comp.imagePdfConversionRequest).toEqual(imagePdfConversionRequest);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePdfConversionRequest>>();
      const imagePdfConversionRequest = { id: 31101 };
      jest.spyOn(imagePdfConversionRequestFormService, 'getImagePdfConversionRequest').mockReturnValue(imagePdfConversionRequest);
      jest.spyOn(imagePdfConversionRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePdfConversionRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePdfConversionRequest }));
      saveSubject.complete();

      // THEN
      expect(imagePdfConversionRequestFormService.getImagePdfConversionRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imagePdfConversionRequestService.update).toHaveBeenCalledWith(expect.objectContaining(imagePdfConversionRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePdfConversionRequest>>();
      const imagePdfConversionRequest = { id: 31101 };
      jest.spyOn(imagePdfConversionRequestFormService, 'getImagePdfConversionRequest').mockReturnValue({ id: null });
      jest.spyOn(imagePdfConversionRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePdfConversionRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePdfConversionRequest }));
      saveSubject.complete();

      // THEN
      expect(imagePdfConversionRequestFormService.getImagePdfConversionRequest).toHaveBeenCalled();
      expect(imagePdfConversionRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePdfConversionRequest>>();
      const imagePdfConversionRequest = { id: 31101 };
      jest.spyOn(imagePdfConversionRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePdfConversionRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imagePdfConversionRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImageConversionBatch', () => {
      it('should forward to imageConversionBatchService', () => {
        const entity = { id: 31426 };
        const entity2 = { id: 12076 };
        jest.spyOn(imageConversionBatchService, 'compareImageConversionBatch');
        comp.compareImageConversionBatch(entity, entity2);
        expect(imageConversionBatchService.compareImageConversionBatch).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
