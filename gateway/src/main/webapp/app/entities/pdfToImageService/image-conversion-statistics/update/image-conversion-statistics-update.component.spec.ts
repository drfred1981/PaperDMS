import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ImageConversionStatisticsService } from '../service/image-conversion-statistics.service';
import { IImageConversionStatistics } from '../image-conversion-statistics.model';
import { ImageConversionStatisticsFormService } from './image-conversion-statistics-form.service';

import { ImageConversionStatisticsUpdateComponent } from './image-conversion-statistics-update.component';

describe('ImageConversionStatistics Management Update Component', () => {
  let comp: ImageConversionStatisticsUpdateComponent;
  let fixture: ComponentFixture<ImageConversionStatisticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageConversionStatisticsFormService: ImageConversionStatisticsFormService;
  let imageConversionStatisticsService: ImageConversionStatisticsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageConversionStatisticsUpdateComponent],
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
      .overrideTemplate(ImageConversionStatisticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageConversionStatisticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageConversionStatisticsFormService = TestBed.inject(ImageConversionStatisticsFormService);
    imageConversionStatisticsService = TestBed.inject(ImageConversionStatisticsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const imageConversionStatistics: IImageConversionStatistics = { id: 29406 };

      activatedRoute.data = of({ imageConversionStatistics });
      comp.ngOnInit();

      expect(comp.imageConversionStatistics).toEqual(imageConversionStatistics);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionStatistics>>();
      const imageConversionStatistics = { id: 23721 };
      jest.spyOn(imageConversionStatisticsFormService, 'getImageConversionStatistics').mockReturnValue(imageConversionStatistics);
      jest.spyOn(imageConversionStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionStatistics }));
      saveSubject.complete();

      // THEN
      expect(imageConversionStatisticsFormService.getImageConversionStatistics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageConversionStatisticsService.update).toHaveBeenCalledWith(expect.objectContaining(imageConversionStatistics));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionStatistics>>();
      const imageConversionStatistics = { id: 23721 };
      jest.spyOn(imageConversionStatisticsFormService, 'getImageConversionStatistics').mockReturnValue({ id: null });
      jest.spyOn(imageConversionStatisticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionStatistics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionStatistics }));
      saveSubject.complete();

      // THEN
      expect(imageConversionStatisticsFormService.getImageConversionStatistics).toHaveBeenCalled();
      expect(imageConversionStatisticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionStatistics>>();
      const imageConversionStatistics = { id: 23721 };
      jest.spyOn(imageConversionStatisticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionStatistics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageConversionStatisticsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
