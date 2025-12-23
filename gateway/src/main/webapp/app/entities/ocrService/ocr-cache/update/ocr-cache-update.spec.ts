import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOcrCache } from '../ocr-cache.model';
import { OcrCacheService } from '../service/ocr-cache.service';

import { OcrCacheFormService } from './ocr-cache-form.service';
import { OcrCacheUpdate } from './ocr-cache-update';

describe('OcrCache Management Update Component', () => {
  let comp: OcrCacheUpdate;
  let fixture: ComponentFixture<OcrCacheUpdate>;
  let activatedRoute: ActivatedRoute;
  let ocrCacheFormService: OcrCacheFormService;
  let ocrCacheService: OcrCacheService;

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

    fixture = TestBed.createComponent(OcrCacheUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ocrCacheFormService = TestBed.inject(OcrCacheFormService);
    ocrCacheService = TestBed.inject(OcrCacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const ocrCache: IOcrCache = { id: 11186 };

      activatedRoute.data = of({ ocrCache });
      comp.ngOnInit();

      expect(comp.ocrCache).toEqual(ocrCache);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrCache>>();
      const ocrCache = { id: 3253 };
      jest.spyOn(ocrCacheFormService, 'getOcrCache').mockReturnValue(ocrCache);
      jest.spyOn(ocrCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrCache }));
      saveSubject.complete();

      // THEN
      expect(ocrCacheFormService.getOcrCache).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ocrCacheService.update).toHaveBeenCalledWith(expect.objectContaining(ocrCache));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrCache>>();
      const ocrCache = { id: 3253 };
      jest.spyOn(ocrCacheFormService, 'getOcrCache').mockReturnValue({ id: null });
      jest.spyOn(ocrCacheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrCache: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrCache }));
      saveSubject.complete();

      // THEN
      expect(ocrCacheFormService.getOcrCache).toHaveBeenCalled();
      expect(ocrCacheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrCache>>();
      const ocrCache = { id: 3253 };
      jest.spyOn(ocrCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ocrCacheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
