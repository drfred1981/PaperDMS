import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IScanBatch } from '../scan-batch.model';
import { ScanBatchService } from '../service/scan-batch.service';

import { ScanBatchFormService } from './scan-batch-form.service';
import { ScanBatchUpdate } from './scan-batch-update';

describe('ScanBatch Management Update Component', () => {
  let comp: ScanBatchUpdate;
  let fixture: ComponentFixture<ScanBatchUpdate>;
  let activatedRoute: ActivatedRoute;
  let scanBatchFormService: ScanBatchFormService;
  let scanBatchService: ScanBatchService;

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

    fixture = TestBed.createComponent(ScanBatchUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scanBatchFormService = TestBed.inject(ScanBatchFormService);
    scanBatchService = TestBed.inject(ScanBatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const scanBatch: IScanBatch = { id: 11487 };

      activatedRoute.data = of({ scanBatch });
      comp.ngOnInit();

      expect(comp.scanBatch).toEqual(scanBatch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanBatch>>();
      const scanBatch = { id: 9534 };
      jest.spyOn(scanBatchFormService, 'getScanBatch').mockReturnValue(scanBatch);
      jest.spyOn(scanBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scanBatch }));
      saveSubject.complete();

      // THEN
      expect(scanBatchFormService.getScanBatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scanBatchService.update).toHaveBeenCalledWith(expect.objectContaining(scanBatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanBatch>>();
      const scanBatch = { id: 9534 };
      jest.spyOn(scanBatchFormService, 'getScanBatch').mockReturnValue({ id: null });
      jest.spyOn(scanBatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanBatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scanBatch }));
      saveSubject.complete();

      // THEN
      expect(scanBatchFormService.getScanBatch).toHaveBeenCalled();
      expect(scanBatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScanBatch>>();
      const scanBatch = { id: 9534 };
      jest.spyOn(scanBatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scanBatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scanBatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
