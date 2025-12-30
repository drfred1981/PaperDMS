import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ImageConversionHistoryService } from '../service/image-conversion-history.service';
import { IImageConversionHistory } from '../image-conversion-history.model';
import { ImageConversionHistoryFormService } from './image-conversion-history-form.service';

import { ImageConversionHistoryUpdateComponent } from './image-conversion-history-update.component';

describe('ImageConversionHistory Management Update Component', () => {
  let comp: ImageConversionHistoryUpdateComponent;
  let fixture: ComponentFixture<ImageConversionHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageConversionHistoryFormService: ImageConversionHistoryFormService;
  let imageConversionHistoryService: ImageConversionHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageConversionHistoryUpdateComponent],
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
      .overrideTemplate(ImageConversionHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageConversionHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageConversionHistoryFormService = TestBed.inject(ImageConversionHistoryFormService);
    imageConversionHistoryService = TestBed.inject(ImageConversionHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const imageConversionHistory: IImageConversionHistory = { id: 8118 };

      activatedRoute.data = of({ imageConversionHistory });
      comp.ngOnInit();

      expect(comp.imageConversionHistory).toEqual(imageConversionHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionHistory>>();
      const imageConversionHistory = { id: 7139 };
      jest.spyOn(imageConversionHistoryFormService, 'getImageConversionHistory').mockReturnValue(imageConversionHistory);
      jest.spyOn(imageConversionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionHistory }));
      saveSubject.complete();

      // THEN
      expect(imageConversionHistoryFormService.getImageConversionHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageConversionHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(imageConversionHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionHistory>>();
      const imageConversionHistory = { id: 7139 };
      jest.spyOn(imageConversionHistoryFormService, 'getImageConversionHistory').mockReturnValue({ id: null });
      jest.spyOn(imageConversionHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionHistory }));
      saveSubject.complete();

      // THEN
      expect(imageConversionHistoryFormService.getImageConversionHistory).toHaveBeenCalled();
      expect(imageConversionHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionHistory>>();
      const imageConversionHistory = { id: 7139 };
      jest.spyOn(imageConversionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageConversionHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
