import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IScanJob } from 'app/entities/scanService/scan-job/scan-job.model';
import { ScanJobService } from 'app/entities/scanService/scan-job/service/scan-job.service';
import { IScannedPage } from '../scanned-page.model';
import { ScannedPageService } from '../service/scanned-page.service';

import { ScannedPageFormService } from './scanned-page-form.service';
import { ScannedPageUpdate } from './scanned-page-update';

describe('ScannedPage Management Update Component', () => {
  let comp: ScannedPageUpdate;
  let fixture: ComponentFixture<ScannedPageUpdate>;
  let activatedRoute: ActivatedRoute;
  let scannedPageFormService: ScannedPageFormService;
  let scannedPageService: ScannedPageService;
  let scanJobService: ScanJobService;

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

    fixture = TestBed.createComponent(ScannedPageUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scannedPageFormService = TestBed.inject(ScannedPageFormService);
    scannedPageService = TestBed.inject(ScannedPageService);
    scanJobService = TestBed.inject(ScanJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ScanJob query and add missing value', () => {
      const scannedPage: IScannedPage = { id: 12588 };
      const scanJob: IScanJob = { id: 3321 };
      scannedPage.scanJob = scanJob;

      const scanJobCollection: IScanJob[] = [{ id: 3321 }];
      jest.spyOn(scanJobService, 'query').mockReturnValue(of(new HttpResponse({ body: scanJobCollection })));
      const additionalScanJobs = [scanJob];
      const expectedCollection: IScanJob[] = [...additionalScanJobs, ...scanJobCollection];
      jest.spyOn(scanJobService, 'addScanJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scannedPage });
      comp.ngOnInit();

      expect(scanJobService.query).toHaveBeenCalled();
      expect(scanJobService.addScanJobToCollectionIfMissing).toHaveBeenCalledWith(
        scanJobCollection,
        ...additionalScanJobs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.scanJobsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const scannedPage: IScannedPage = { id: 12588 };
      const scanJob: IScanJob = { id: 3321 };
      scannedPage.scanJob = scanJob;

      activatedRoute.data = of({ scannedPage });
      comp.ngOnInit();

      expect(comp.scanJobsSharedCollection()).toContainEqual(scanJob);
      expect(comp.scannedPage).toEqual(scannedPage);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannedPage>>();
      const scannedPage = { id: 4792 };
      jest.spyOn(scannedPageFormService, 'getScannedPage').mockReturnValue(scannedPage);
      jest.spyOn(scannedPageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannedPage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scannedPage }));
      saveSubject.complete();

      // THEN
      expect(scannedPageFormService.getScannedPage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scannedPageService.update).toHaveBeenCalledWith(expect.objectContaining(scannedPage));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannedPage>>();
      const scannedPage = { id: 4792 };
      jest.spyOn(scannedPageFormService, 'getScannedPage').mockReturnValue({ id: null });
      jest.spyOn(scannedPageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannedPage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scannedPage }));
      saveSubject.complete();

      // THEN
      expect(scannedPageFormService.getScannedPage).toHaveBeenCalled();
      expect(scannedPageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannedPage>>();
      const scannedPage = { id: 4792 };
      jest.spyOn(scannedPageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannedPage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scannedPageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareScanJob', () => {
      it('should forward to scanJobService', () => {
        const entity = { id: 3321 };
        const entity2 = { id: 5501 };
        jest.spyOn(scanJobService, 'compareScanJob');
        comp.compareScanJob(entity, entity2);
        expect(scanJobService.compareScanJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
