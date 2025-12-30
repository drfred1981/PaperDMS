import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MonitoringDocumentWatchService } from '../service/monitoring-document-watch.service';
import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';
import { MonitoringDocumentWatchFormService } from './monitoring-document-watch-form.service';

import { MonitoringDocumentWatchUpdateComponent } from './monitoring-document-watch-update.component';

describe('MonitoringDocumentWatch Management Update Component', () => {
  let comp: MonitoringDocumentWatchUpdateComponent;
  let fixture: ComponentFixture<MonitoringDocumentWatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringDocumentWatchFormService: MonitoringDocumentWatchFormService;
  let monitoringDocumentWatchService: MonitoringDocumentWatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringDocumentWatchUpdateComponent],
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
      .overrideTemplate(MonitoringDocumentWatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringDocumentWatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringDocumentWatchFormService = TestBed.inject(MonitoringDocumentWatchFormService);
    monitoringDocumentWatchService = TestBed.inject(MonitoringDocumentWatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const monitoringDocumentWatch: IMonitoringDocumentWatch = { id: 17970 };

      activatedRoute.data = of({ monitoringDocumentWatch });
      comp.ngOnInit();

      expect(comp.monitoringDocumentWatch).toEqual(monitoringDocumentWatch);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringDocumentWatch>>();
      const monitoringDocumentWatch = { id: 10460 };
      jest.spyOn(monitoringDocumentWatchFormService, 'getMonitoringDocumentWatch').mockReturnValue(monitoringDocumentWatch);
      jest.spyOn(monitoringDocumentWatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringDocumentWatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringDocumentWatch }));
      saveSubject.complete();

      // THEN
      expect(monitoringDocumentWatchFormService.getMonitoringDocumentWatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringDocumentWatchService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringDocumentWatch));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringDocumentWatch>>();
      const monitoringDocumentWatch = { id: 10460 };
      jest.spyOn(monitoringDocumentWatchFormService, 'getMonitoringDocumentWatch').mockReturnValue({ id: null });
      jest.spyOn(monitoringDocumentWatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringDocumentWatch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringDocumentWatch }));
      saveSubject.complete();

      // THEN
      expect(monitoringDocumentWatchFormService.getMonitoringDocumentWatch).toHaveBeenCalled();
      expect(monitoringDocumentWatchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringDocumentWatch>>();
      const monitoringDocumentWatch = { id: 10460 };
      jest.spyOn(monitoringDocumentWatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringDocumentWatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringDocumentWatchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
