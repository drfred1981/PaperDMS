import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IImagePdfConversionRequest } from 'app/entities/pdfToImageService/image-pdf-conversion-request/image-pdf-conversion-request.model';
import { ImagePdfConversionRequestService } from 'app/entities/pdfToImageService/image-pdf-conversion-request/service/image-pdf-conversion-request.service';
import { ImageGeneratedImageService } from '../service/image-generated-image.service';
import { IImageGeneratedImage } from '../image-generated-image.model';
import { ImageGeneratedImageFormService } from './image-generated-image-form.service';

import { ImageGeneratedImageUpdateComponent } from './image-generated-image-update.component';

describe('ImageGeneratedImage Management Update Component', () => {
  let comp: ImageGeneratedImageUpdateComponent;
  let fixture: ComponentFixture<ImageGeneratedImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageGeneratedImageFormService: ImageGeneratedImageFormService;
  let imageGeneratedImageService: ImageGeneratedImageService;
  let imagePdfConversionRequestService: ImagePdfConversionRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageGeneratedImageUpdateComponent],
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
      .overrideTemplate(ImageGeneratedImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageGeneratedImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageGeneratedImageFormService = TestBed.inject(ImageGeneratedImageFormService);
    imageGeneratedImageService = TestBed.inject(ImageGeneratedImageService);
    imagePdfConversionRequestService = TestBed.inject(ImagePdfConversionRequestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ImagePdfConversionRequest query and add missing value', () => {
      const imageGeneratedImage: IImageGeneratedImage = { id: 9650 };
      const conversionRequest: IImagePdfConversionRequest = { id: 31101 };
      imageGeneratedImage.conversionRequest = conversionRequest;

      const imagePdfConversionRequestCollection: IImagePdfConversionRequest[] = [{ id: 31101 }];
      jest
        .spyOn(imagePdfConversionRequestService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: imagePdfConversionRequestCollection })));
      const additionalImagePdfConversionRequests = [conversionRequest];
      const expectedCollection: IImagePdfConversionRequest[] = [
        ...additionalImagePdfConversionRequests,
        ...imagePdfConversionRequestCollection,
      ];
      jest.spyOn(imagePdfConversionRequestService, 'addImagePdfConversionRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imageGeneratedImage });
      comp.ngOnInit();

      expect(imagePdfConversionRequestService.query).toHaveBeenCalled();
      expect(imagePdfConversionRequestService.addImagePdfConversionRequestToCollectionIfMissing).toHaveBeenCalledWith(
        imagePdfConversionRequestCollection,
        ...additionalImagePdfConversionRequests.map(expect.objectContaining),
      );
      expect(comp.imagePdfConversionRequestsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const imageGeneratedImage: IImageGeneratedImage = { id: 9650 };
      const conversionRequest: IImagePdfConversionRequest = { id: 31101 };
      imageGeneratedImage.conversionRequest = conversionRequest;

      activatedRoute.data = of({ imageGeneratedImage });
      comp.ngOnInit();

      expect(comp.imagePdfConversionRequestsSharedCollection).toContainEqual(conversionRequest);
      expect(comp.imageGeneratedImage).toEqual(imageGeneratedImage);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageGeneratedImage>>();
      const imageGeneratedImage = { id: 9352 };
      jest.spyOn(imageGeneratedImageFormService, 'getImageGeneratedImage').mockReturnValue(imageGeneratedImage);
      jest.spyOn(imageGeneratedImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageGeneratedImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageGeneratedImage }));
      saveSubject.complete();

      // THEN
      expect(imageGeneratedImageFormService.getImageGeneratedImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageGeneratedImageService.update).toHaveBeenCalledWith(expect.objectContaining(imageGeneratedImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageGeneratedImage>>();
      const imageGeneratedImage = { id: 9352 };
      jest.spyOn(imageGeneratedImageFormService, 'getImageGeneratedImage').mockReturnValue({ id: null });
      jest.spyOn(imageGeneratedImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageGeneratedImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageGeneratedImage }));
      saveSubject.complete();

      // THEN
      expect(imageGeneratedImageFormService.getImageGeneratedImage).toHaveBeenCalled();
      expect(imageGeneratedImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageGeneratedImage>>();
      const imageGeneratedImage = { id: 9352 };
      jest.spyOn(imageGeneratedImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageGeneratedImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageGeneratedImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImagePdfConversionRequest', () => {
      it('should forward to imagePdfConversionRequestService', () => {
        const entity = { id: 31101 };
        const entity2 = { id: 32495 };
        jest.spyOn(imagePdfConversionRequestService, 'compareImagePdfConversionRequest');
        comp.compareImagePdfConversionRequest(entity, entity2);
        expect(imagePdfConversionRequestService.compareImagePdfConversionRequest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
