import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExportJob } from 'app/entities/exportService/export-job/export-job.model';
import { ExportJobService } from 'app/entities/exportService/export-job/service/export-job.service';
import { ExportResultService } from '../service/export-result.service';
import { IExportResult } from '../export-result.model';
import { ExportResultFormService } from './export-result-form.service';

import { ExportResultUpdateComponent } from './export-result-update.component';

describe('ExportResult Management Update Component', () => {
  let comp: ExportResultUpdateComponent;
  let fixture: ComponentFixture<ExportResultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exportResultFormService: ExportResultFormService;
  let exportResultService: ExportResultService;
  let exportJobService: ExportJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExportResultUpdateComponent],
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
      .overrideTemplate(ExportResultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExportResultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exportResultFormService = TestBed.inject(ExportResultFormService);
    exportResultService = TestBed.inject(ExportResultService);
    exportJobService = TestBed.inject(ExportJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ExportJob query and add missing value', () => {
      const exportResult: IExportResult = { id: 16132 };
      const exportJob: IExportJob = { id: 4139 };
      exportResult.exportJob = exportJob;

      const exportJobCollection: IExportJob[] = [{ id: 4139 }];
      jest.spyOn(exportJobService, 'query').mockReturnValue(of(new HttpResponse({ body: exportJobCollection })));
      const additionalExportJobs = [exportJob];
      const expectedCollection: IExportJob[] = [...additionalExportJobs, ...exportJobCollection];
      jest.spyOn(exportJobService, 'addExportJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exportResult });
      comp.ngOnInit();

      expect(exportJobService.query).toHaveBeenCalled();
      expect(exportJobService.addExportJobToCollectionIfMissing).toHaveBeenCalledWith(
        exportJobCollection,
        ...additionalExportJobs.map(expect.objectContaining),
      );
      expect(comp.exportJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const exportResult: IExportResult = { id: 16132 };
      const exportJob: IExportJob = { id: 4139 };
      exportResult.exportJob = exportJob;

      activatedRoute.data = of({ exportResult });
      comp.ngOnInit();

      expect(comp.exportJobsSharedCollection).toContainEqual(exportJob);
      expect(comp.exportResult).toEqual(exportResult);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportResult>>();
      const exportResult = { id: 21181 };
      jest.spyOn(exportResultFormService, 'getExportResult').mockReturnValue(exportResult);
      jest.spyOn(exportResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportResult }));
      saveSubject.complete();

      // THEN
      expect(exportResultFormService.getExportResult).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(exportResultService.update).toHaveBeenCalledWith(expect.objectContaining(exportResult));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportResult>>();
      const exportResult = { id: 21181 };
      jest.spyOn(exportResultFormService, 'getExportResult').mockReturnValue({ id: null });
      jest.spyOn(exportResultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportResult: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportResult }));
      saveSubject.complete();

      // THEN
      expect(exportResultFormService.getExportResult).toHaveBeenCalled();
      expect(exportResultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportResult>>();
      const exportResult = { id: 21181 };
      jest.spyOn(exportResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exportResultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExportJob', () => {
      it('should forward to exportJobService', () => {
        const entity = { id: 4139 };
        const entity2 = { id: 24008 };
        jest.spyOn(exportJobService, 'compareExportJob');
        comp.compareExportJob(entity, entity2);
        expect(exportJobService.compareExportJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
