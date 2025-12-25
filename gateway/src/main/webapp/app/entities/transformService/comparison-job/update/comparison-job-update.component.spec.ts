import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ComparisonJobService } from '../service/comparison-job.service';
import { IComparisonJob } from '../comparison-job.model';
import { ComparisonJobFormService } from './comparison-job-form.service';

import { ComparisonJobUpdateComponent } from './comparison-job-update.component';

describe('ComparisonJob Management Update Component', () => {
  let comp: ComparisonJobUpdateComponent;
  let fixture: ComponentFixture<ComparisonJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comparisonJobFormService: ComparisonJobFormService;
  let comparisonJobService: ComparisonJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ComparisonJobUpdateComponent],
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
      .overrideTemplate(ComparisonJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComparisonJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comparisonJobFormService = TestBed.inject(ComparisonJobFormService);
    comparisonJobService = TestBed.inject(ComparisonJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const comparisonJob: IComparisonJob = { id: 31301 };

      activatedRoute.data = of({ comparisonJob });
      comp.ngOnInit();

      expect(comp.comparisonJob).toEqual(comparisonJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComparisonJob>>();
      const comparisonJob = { id: 29962 };
      jest.spyOn(comparisonJobFormService, 'getComparisonJob').mockReturnValue(comparisonJob);
      jest.spyOn(comparisonJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comparisonJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comparisonJob }));
      saveSubject.complete();

      // THEN
      expect(comparisonJobFormService.getComparisonJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(comparisonJobService.update).toHaveBeenCalledWith(expect.objectContaining(comparisonJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComparisonJob>>();
      const comparisonJob = { id: 29962 };
      jest.spyOn(comparisonJobFormService, 'getComparisonJob').mockReturnValue({ id: null });
      jest.spyOn(comparisonJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comparisonJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comparisonJob }));
      saveSubject.complete();

      // THEN
      expect(comparisonJobFormService.getComparisonJob).toHaveBeenCalled();
      expect(comparisonJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComparisonJob>>();
      const comparisonJob = { id: 29962 };
      jest.spyOn(comparisonJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comparisonJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comparisonJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
