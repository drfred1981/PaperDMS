import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExportPattern } from 'app/entities/exportService/export-pattern/export-pattern.model';
import { ExportPatternService } from 'app/entities/exportService/export-pattern/service/export-pattern.service';
import { ExportJobService } from '../service/export-job.service';
import { IExportJob } from '../export-job.model';
import { ExportJobFormService } from './export-job-form.service';

import { ExportJobUpdateComponent } from './export-job-update.component';

describe('ExportJob Management Update Component', () => {
  let comp: ExportJobUpdateComponent;
  let fixture: ComponentFixture<ExportJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exportJobFormService: ExportJobFormService;
  let exportJobService: ExportJobService;
  let exportPatternService: ExportPatternService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExportJobUpdateComponent],
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
      .overrideTemplate(ExportJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExportJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exportJobFormService = TestBed.inject(ExportJobFormService);
    exportJobService = TestBed.inject(ExportJobService);
    exportPatternService = TestBed.inject(ExportPatternService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ExportPattern query and add missing value', () => {
      const exportJob: IExportJob = { id: 24008 };
      const exportPattern: IExportPattern = { id: 13725 };
      exportJob.exportPattern = exportPattern;

      const exportPatternCollection: IExportPattern[] = [{ id: 13725 }];
      jest.spyOn(exportPatternService, 'query').mockReturnValue(of(new HttpResponse({ body: exportPatternCollection })));
      const additionalExportPatterns = [exportPattern];
      const expectedCollection: IExportPattern[] = [...additionalExportPatterns, ...exportPatternCollection];
      jest.spyOn(exportPatternService, 'addExportPatternToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exportJob });
      comp.ngOnInit();

      expect(exportPatternService.query).toHaveBeenCalled();
      expect(exportPatternService.addExportPatternToCollectionIfMissing).toHaveBeenCalledWith(
        exportPatternCollection,
        ...additionalExportPatterns.map(expect.objectContaining),
      );
      expect(comp.exportPatternsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const exportJob: IExportJob = { id: 24008 };
      const exportPattern: IExportPattern = { id: 13725 };
      exportJob.exportPattern = exportPattern;

      activatedRoute.data = of({ exportJob });
      comp.ngOnInit();

      expect(comp.exportPatternsSharedCollection).toContainEqual(exportPattern);
      expect(comp.exportJob).toEqual(exportJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportJob>>();
      const exportJob = { id: 4139 };
      jest.spyOn(exportJobFormService, 'getExportJob').mockReturnValue(exportJob);
      jest.spyOn(exportJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportJob }));
      saveSubject.complete();

      // THEN
      expect(exportJobFormService.getExportJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(exportJobService.update).toHaveBeenCalledWith(expect.objectContaining(exportJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportJob>>();
      const exportJob = { id: 4139 };
      jest.spyOn(exportJobFormService, 'getExportJob').mockReturnValue({ id: null });
      jest.spyOn(exportJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportJob }));
      saveSubject.complete();

      // THEN
      expect(exportJobFormService.getExportJob).toHaveBeenCalled();
      expect(exportJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportJob>>();
      const exportJob = { id: 4139 };
      jest.spyOn(exportJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exportJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExportPattern', () => {
      it('should forward to exportPatternService', () => {
        const entity = { id: 13725 };
        const entity2 = { id: 20418 };
        jest.spyOn(exportPatternService, 'compareExportPattern');
        comp.compareExportPattern(entity, entity2);
        expect(exportPatternService.compareExportPattern).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
