import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IScannerConfiguration } from '../scanner-configuration.model';
import { ScannerConfigurationService } from '../service/scanner-configuration.service';

import { ScannerConfigurationFormService } from './scanner-configuration-form.service';
import { ScannerConfigurationUpdate } from './scanner-configuration-update';

describe('ScannerConfiguration Management Update Component', () => {
  let comp: ScannerConfigurationUpdate;
  let fixture: ComponentFixture<ScannerConfigurationUpdate>;
  let activatedRoute: ActivatedRoute;
  let scannerConfigurationFormService: ScannerConfigurationFormService;
  let scannerConfigurationService: ScannerConfigurationService;

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

    fixture = TestBed.createComponent(ScannerConfigurationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scannerConfigurationFormService = TestBed.inject(ScannerConfigurationFormService);
    scannerConfigurationService = TestBed.inject(ScannerConfigurationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const scannerConfiguration: IScannerConfiguration = { id: 17334 };

      activatedRoute.data = of({ scannerConfiguration });
      comp.ngOnInit();

      expect(comp.scannerConfiguration).toEqual(scannerConfiguration);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannerConfiguration>>();
      const scannerConfiguration = { id: 13848 };
      jest.spyOn(scannerConfigurationFormService, 'getScannerConfiguration').mockReturnValue(scannerConfiguration);
      jest.spyOn(scannerConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannerConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scannerConfiguration }));
      saveSubject.complete();

      // THEN
      expect(scannerConfigurationFormService.getScannerConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scannerConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(scannerConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannerConfiguration>>();
      const scannerConfiguration = { id: 13848 };
      jest.spyOn(scannerConfigurationFormService, 'getScannerConfiguration').mockReturnValue({ id: null });
      jest.spyOn(scannerConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannerConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scannerConfiguration }));
      saveSubject.complete();

      // THEN
      expect(scannerConfigurationFormService.getScannerConfiguration).toHaveBeenCalled();
      expect(scannerConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScannerConfiguration>>();
      const scannerConfiguration = { id: 13848 };
      jest.spyOn(scannerConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scannerConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scannerConfigurationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
