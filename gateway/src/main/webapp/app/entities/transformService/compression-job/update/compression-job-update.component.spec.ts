import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CompressionJobService } from '../service/compression-job.service';
import { ICompressionJob } from '../compression-job.model';
import { CompressionJobFormService } from './compression-job-form.service';

import { CompressionJobUpdateComponent } from './compression-job-update.component';

describe('CompressionJob Management Update Component', () => {
  let comp: CompressionJobUpdateComponent;
  let fixture: ComponentFixture<CompressionJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compressionJobFormService: CompressionJobFormService;
  let compressionJobService: CompressionJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CompressionJobUpdateComponent],
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
      .overrideTemplate(CompressionJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompressionJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compressionJobFormService = TestBed.inject(CompressionJobFormService);
    compressionJobService = TestBed.inject(CompressionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const compressionJob: ICompressionJob = { id: 923 };

      activatedRoute.data = of({ compressionJob });
      comp.ngOnInit();

      expect(comp.compressionJob).toEqual(compressionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompressionJob>>();
      const compressionJob = { id: 5819 };
      jest.spyOn(compressionJobFormService, 'getCompressionJob').mockReturnValue(compressionJob);
      jest.spyOn(compressionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compressionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compressionJob }));
      saveSubject.complete();

      // THEN
      expect(compressionJobFormService.getCompressionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compressionJobService.update).toHaveBeenCalledWith(expect.objectContaining(compressionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompressionJob>>();
      const compressionJob = { id: 5819 };
      jest.spyOn(compressionJobFormService, 'getCompressionJob').mockReturnValue({ id: null });
      jest.spyOn(compressionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compressionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compressionJob }));
      saveSubject.complete();

      // THEN
      expect(compressionJobFormService.getCompressionJob).toHaveBeenCalled();
      expect(compressionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompressionJob>>();
      const compressionJob = { id: 5819 };
      jest.spyOn(compressionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compressionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compressionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
