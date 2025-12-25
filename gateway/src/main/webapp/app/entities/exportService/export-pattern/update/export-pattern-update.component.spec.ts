import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ExportPatternService } from '../service/export-pattern.service';
import { IExportPattern } from '../export-pattern.model';
import { ExportPatternFormService } from './export-pattern-form.service';

import { ExportPatternUpdateComponent } from './export-pattern-update.component';

describe('ExportPattern Management Update Component', () => {
  let comp: ExportPatternUpdateComponent;
  let fixture: ComponentFixture<ExportPatternUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let exportPatternFormService: ExportPatternFormService;
  let exportPatternService: ExportPatternService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExportPatternUpdateComponent],
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
      .overrideTemplate(ExportPatternUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExportPatternUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    exportPatternFormService = TestBed.inject(ExportPatternFormService);
    exportPatternService = TestBed.inject(ExportPatternService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const exportPattern: IExportPattern = { id: 20418 };

      activatedRoute.data = of({ exportPattern });
      comp.ngOnInit();

      expect(comp.exportPattern).toEqual(exportPattern);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportPattern>>();
      const exportPattern = { id: 13725 };
      jest.spyOn(exportPatternFormService, 'getExportPattern').mockReturnValue(exportPattern);
      jest.spyOn(exportPatternService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportPattern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportPattern }));
      saveSubject.complete();

      // THEN
      expect(exportPatternFormService.getExportPattern).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(exportPatternService.update).toHaveBeenCalledWith(expect.objectContaining(exportPattern));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportPattern>>();
      const exportPattern = { id: 13725 };
      jest.spyOn(exportPatternFormService, 'getExportPattern').mockReturnValue({ id: null });
      jest.spyOn(exportPatternService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportPattern: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exportPattern }));
      saveSubject.complete();

      // THEN
      expect(exportPatternFormService.getExportPattern).toHaveBeenCalled();
      expect(exportPatternService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExportPattern>>();
      const exportPattern = { id: 13725 };
      jest.spyOn(exportPatternService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exportPattern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(exportPatternService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
