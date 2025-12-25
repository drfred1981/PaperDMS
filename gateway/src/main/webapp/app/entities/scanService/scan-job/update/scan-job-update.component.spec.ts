import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IScannerConfiguration } from 'app/entities/scanService/scanner-configuration/scanner-configuration.model';
import { ScannerConfigurationService } from 'app/entities/scanService/scanner-configuration/service/scanner-configuration.service';
import { IScanBatch } from 'app/entities/scanService/scan-batch/scan-batch.model';
import { ScanBatchService } from 'app/entities/scanService/scan-batch/service/scan-batch.service';
import { IScanJob } from '../scan-job.model';
import { ScanJobService } from '../service/scan-job.service';
import { ScanJobFormService } from './scan-job-form.service';

import { ScanJobUpdateComponent } from './scan-job-update.component';

describe('ScanJob Management Update Component', () => {
  let comp: ScanJobUpdateComponent;
  let fixture: ComponentFixture<ScanJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let scanJobFormService: ScanJobFormService;
  let scanJobService: ScanJobService;
  let scannerConfigurationService: ScannerConfigurationService;
  let scanBatchService: ScanBatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ScanJobUpdateComponent],
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
      .overrideTemplate(ScanJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScanJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scanJobFormService = TestBed.inject(ScanJobFormService);
    scanJobService = TestBed.inject(ScanJobService);
    scannerConfigurationService = TestBed.inject(ScannerConfigurationService);
    scanBatchService = TestBed.inject(ScanBatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ScannerConfiguration query and add missing value', () => {
      const scanJob: IScanJob = { id: 5501 };
      const scannerConfig: IScannerConfiguration = { id: 13848 };
      scanJob.scannerConfig = scannerConfig;

      const scannerConfigurationCollection: IScannerConfiguration[] = [{ id: 13848 }];
      jest.spyOn(scannerConfigurationService, 'query').mockReturnValue(of(new HttpResponse({ body: scannerConfigurationCollection })));
      const additionalScannerConfigurations = [scannerConfig];
      const expectedCollection: IScannerConfiguration[] = [...additionalScannerConfigurations, ...scannerConfigurationCollection];
      jest.spyOn(scannerConfigurationService, 'addScannerConfigurationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scanJob });
      comp.ngOnInit();

      expect(scannerConfigurationService.query).toHaveBeenCalled();
      expect(scannerConfigurationService.addScannerConfigurationToCollectionIfMissing).toHaveBeenCalledWith(
        scannerConfigurationCollection,
        ...additionalScannerConfigurations.map(expect.objectContaining),
      );
      expect(comp.scannerConfigurationsSharedCollection).toEqual(expectedCollection);
    });

    it('should call ScanBatch query and add missing value', () => {
      const scanJob: IScanJob = { id: 5501 };
      const batch: IScanBatch = { id: 9534 };
      scanJob.batch = batch;

      const scanBatchCollection: IScanBatch[] = [{ id: 9534 }];
      jest.spyOn(scanBatchService, 'query').mockReturnValue(of(new HttpResponse({ body: scanBatchCollection })));
      const additionalScanBatches = [batch];
      const expectedCollection: IScanBatch[] = [...additionalScanBatches, ...scanBatchCollection];
      jest.spyOn(scanBatchService, 'addScanBatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scanJob });
      comp.ngOnInit();

      expect(scanBatchService.query).toHaveBeenCalled();
      expect(scanBatchService.addScanBatchToCollectionIfMissing).toHaveBeenCalledWith(
        scanBatchCollection,
        ...additionalScanBatches.map(expect.objectContaining),
      );
      expect(comp.scanBatchesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const scanJob: IScanJob = { id: 5501 };
      const scannerConfig: IScannerConfiguration = { id: 13848 };
      scanJob.scannerConfig = scannerConfig;
      const batch: IScanBatch = { id: 9534 };
      scanJob.batch = batch;

      activatedRoute.data = of({ scanJob });
      comp.ngOnInit();

      expect(comp.scannerConfigurationsSharedCollection).toContainEqual(scannerConfig);
      expect(comp.scanBatchesSharedCollection).toContainEqual(batch);
      expect(comp.scanJob).toEqual(scanJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanJob>>();
      const scanJob = { id: 3321 };
      jest.spyOn(scanJobFormService, 'getScanJob').mockReturnValue(scanJob);
      jest.spyOn(scanJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scanJob }));
      saveSubject.complete();

      // THEN
      expect(scanJobFormService.getScanJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scanJobService.update).toHaveBeenCalledWith(expect.objectContaining(scanJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanJob>>();
      const scanJob = { id: 3321 };
      jest.spyOn(scanJobFormService, 'getScanJob').mockReturnValue({ id: null });
      jest.spyOn(scanJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scanJob }));
      saveSubject.complete();

      // THEN
      expect(scanJobFormService.getScanJob).toHaveBeenCalled();
      expect(scanJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanJob>>();
      const scanJob = { id: 3321 };
      jest.spyOn(scanJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scanJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareScannerConfiguration', () => {
      it('should forward to scannerConfigurationService', () => {
        const entity = { id: 13848 };
        const entity2 = { id: 17334 };
        jest.spyOn(scannerConfigurationService, 'compareScannerConfiguration');
        comp.compareScannerConfiguration(entity, entity2);
        expect(scannerConfigurationService.compareScannerConfiguration).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareScanBatch', () => {
      it('should forward to scanBatchService', () => {
        const entity = { id: 9534 };
        const entity2 = { id: 11487 };
        jest.spyOn(scanBatchService, 'compareScanBatch');
        comp.compareScanBatch(entity, entity2);
        expect(scanBatchService.compareScanBatch).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
