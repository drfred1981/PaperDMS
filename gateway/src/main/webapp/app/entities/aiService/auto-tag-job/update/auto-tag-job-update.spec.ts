import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAutoTagJob } from '../auto-tag-job.model';
import { AutoTagJobService } from '../service/auto-tag-job.service';

import { AutoTagJobFormService } from './auto-tag-job-form.service';
import { AutoTagJobUpdate } from './auto-tag-job-update';

describe('AutoTagJob Management Update Component', () => {
  let comp: AutoTagJobUpdate;
  let fixture: ComponentFixture<AutoTagJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let autoTagJobFormService: AutoTagJobFormService;
  let autoTagJobService: AutoTagJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(AutoTagJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    autoTagJobFormService = TestBed.inject(AutoTagJobFormService);
    autoTagJobService = TestBed.inject(AutoTagJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const autoTagJob: IAutoTagJob = { id: 3881 };

      activatedRoute.data = of({ autoTagJob });
      comp.ngOnInit();

      expect(comp.autoTagJob).toEqual(autoTagJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutoTagJob>>();
      const autoTagJob = { id: 5115 };
      jest.spyOn(autoTagJobFormService, 'getAutoTagJob').mockReturnValue(autoTagJob);
      jest.spyOn(autoTagJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autoTagJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: autoTagJob }));
      saveSubject.complete();

      // THEN
      expect(autoTagJobFormService.getAutoTagJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(autoTagJobService.update).toHaveBeenCalledWith(expect.objectContaining(autoTagJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutoTagJob>>();
      const autoTagJob = { id: 5115 };
      jest.spyOn(autoTagJobFormService, 'getAutoTagJob').mockReturnValue({ id: null });
      jest.spyOn(autoTagJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autoTagJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: autoTagJob }));
      saveSubject.complete();

      // THEN
      expect(autoTagJobFormService.getAutoTagJob).toHaveBeenCalled();
      expect(autoTagJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutoTagJob>>();
      const autoTagJob = { id: 5115 };
      jest.spyOn(autoTagJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autoTagJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(autoTagJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
