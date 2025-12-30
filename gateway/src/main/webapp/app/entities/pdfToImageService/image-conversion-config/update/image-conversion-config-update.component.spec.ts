import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ImageConversionConfigService } from '../service/image-conversion-config.service';
import { IImageConversionConfig } from '../image-conversion-config.model';
import { ImageConversionConfigFormService } from './image-conversion-config-form.service';

import { ImageConversionConfigUpdateComponent } from './image-conversion-config-update.component';

describe('ImageConversionConfig Management Update Component', () => {
  let comp: ImageConversionConfigUpdateComponent;
  let fixture: ComponentFixture<ImageConversionConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageConversionConfigFormService: ImageConversionConfigFormService;
  let imageConversionConfigService: ImageConversionConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImageConversionConfigUpdateComponent],
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
      .overrideTemplate(ImageConversionConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageConversionConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageConversionConfigFormService = TestBed.inject(ImageConversionConfigFormService);
    imageConversionConfigService = TestBed.inject(ImageConversionConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const imageConversionConfig: IImageConversionConfig = { id: 29731 };

      activatedRoute.data = of({ imageConversionConfig });
      comp.ngOnInit();

      expect(comp.imageConversionConfig).toEqual(imageConversionConfig);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionConfig>>();
      const imageConversionConfig = { id: 9973 };
      jest.spyOn(imageConversionConfigFormService, 'getImageConversionConfig').mockReturnValue(imageConversionConfig);
      jest.spyOn(imageConversionConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionConfig }));
      saveSubject.complete();

      // THEN
      expect(imageConversionConfigFormService.getImageConversionConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageConversionConfigService.update).toHaveBeenCalledWith(expect.objectContaining(imageConversionConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionConfig>>();
      const imageConversionConfig = { id: 9973 };
      jest.spyOn(imageConversionConfigFormService, 'getImageConversionConfig').mockReturnValue({ id: null });
      jest.spyOn(imageConversionConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageConversionConfig }));
      saveSubject.complete();

      // THEN
      expect(imageConversionConfigFormService.getImageConversionConfig).toHaveBeenCalled();
      expect(imageConversionConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageConversionConfig>>();
      const imageConversionConfig = { id: 9973 };
      jest.spyOn(imageConversionConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageConversionConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageConversionConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
