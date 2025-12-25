import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ImportRuleService } from '../service/import-rule.service';
import { IImportRule } from '../import-rule.model';
import { ImportRuleFormService } from './import-rule-form.service';

import { ImportRuleUpdateComponent } from './import-rule-update.component';

describe('ImportRule Management Update Component', () => {
  let comp: ImportRuleUpdateComponent;
  let fixture: ComponentFixture<ImportRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let importRuleFormService: ImportRuleFormService;
  let importRuleService: ImportRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImportRuleUpdateComponent],
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
      .overrideTemplate(ImportRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImportRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    importRuleFormService = TestBed.inject(ImportRuleFormService);
    importRuleService = TestBed.inject(ImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const importRule: IImportRule = { id: 22098 };

      activatedRoute.data = of({ importRule });
      comp.ngOnInit();

      expect(comp.importRule).toEqual(importRule);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportRule>>();
      const importRule = { id: 3928 };
      jest.spyOn(importRuleFormService, 'getImportRule').mockReturnValue(importRule);
      jest.spyOn(importRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: importRule }));
      saveSubject.complete();

      // THEN
      expect(importRuleFormService.getImportRule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(importRuleService.update).toHaveBeenCalledWith(expect.objectContaining(importRule));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportRule>>();
      const importRule = { id: 3928 };
      jest.spyOn(importRuleFormService, 'getImportRule').mockReturnValue({ id: null });
      jest.spyOn(importRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importRule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: importRule }));
      saveSubject.complete();

      // THEN
      expect(importRuleFormService.getImportRule).toHaveBeenCalled();
      expect(importRuleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportRule>>();
      const importRule = { id: 3928 };
      jest.spyOn(importRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(importRuleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
