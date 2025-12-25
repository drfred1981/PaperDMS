import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WatermarkJobService } from '../service/watermark-job.service';
import { IWatermarkJob } from '../watermark-job.model';
import { WatermarkJobFormService } from './watermark-job-form.service';

import { WatermarkJobUpdateComponent } from './watermark-job-update.component';

describe('WatermarkJob Management Update Component', () => {
  let comp: WatermarkJobUpdateComponent;
  let fixture: ComponentFixture<WatermarkJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let watermarkJobFormService: WatermarkJobFormService;
  let watermarkJobService: WatermarkJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WatermarkJobUpdateComponent],
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
      .overrideTemplate(WatermarkJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WatermarkJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    watermarkJobFormService = TestBed.inject(WatermarkJobFormService);
    watermarkJobService = TestBed.inject(WatermarkJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const watermarkJob: IWatermarkJob = { id: 21229 };

      activatedRoute.data = of({ watermarkJob });
      comp.ngOnInit();

      expect(comp.watermarkJob).toEqual(watermarkJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWatermarkJob>>();
      const watermarkJob = { id: 19288 };
      jest.spyOn(watermarkJobFormService, 'getWatermarkJob').mockReturnValue(watermarkJob);
      jest.spyOn(watermarkJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ watermarkJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: watermarkJob }));
      saveSubject.complete();

      // THEN
      expect(watermarkJobFormService.getWatermarkJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(watermarkJobService.update).toHaveBeenCalledWith(expect.objectContaining(watermarkJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWatermarkJob>>();
      const watermarkJob = { id: 19288 };
      jest.spyOn(watermarkJobFormService, 'getWatermarkJob').mockReturnValue({ id: null });
      jest.spyOn(watermarkJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ watermarkJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: watermarkJob }));
      saveSubject.complete();

      // THEN
      expect(watermarkJobFormService.getWatermarkJob).toHaveBeenCalled();
      expect(watermarkJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWatermarkJob>>();
      const watermarkJob = { id: 19288 };
      jest.spyOn(watermarkJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ watermarkJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(watermarkJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
