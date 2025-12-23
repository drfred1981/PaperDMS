import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IImportRule } from 'app/entities/emailImportService/import-rule/import-rule.model';
import { ImportRuleService } from 'app/entities/emailImportService/import-rule/service/import-rule.service';
import { IImportMapping } from '../import-mapping.model';
import { ImportMappingService } from '../service/import-mapping.service';

import { ImportMappingFormService } from './import-mapping-form.service';
import { ImportMappingUpdate } from './import-mapping-update';

describe('ImportMapping Management Update Component', () => {
  let comp: ImportMappingUpdate;
  let fixture: ComponentFixture<ImportMappingUpdate>;
  let activatedRoute: ActivatedRoute;
  let importMappingFormService: ImportMappingFormService;
  let importMappingService: ImportMappingService;
  let importRuleService: ImportRuleService;

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

    fixture = TestBed.createComponent(ImportMappingUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    importMappingFormService = TestBed.inject(ImportMappingFormService);
    importMappingService = TestBed.inject(ImportMappingService);
    importRuleService = TestBed.inject(ImportRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ImportRule query and add missing value', () => {
      const importMapping: IImportMapping = { id: 3649 };
      const rule: IImportRule = { id: 3928 };
      importMapping.rule = rule;

      const importRuleCollection: IImportRule[] = [{ id: 3928 }];
      jest.spyOn(importRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: importRuleCollection })));
      const additionalImportRules = [rule];
      const expectedCollection: IImportRule[] = [...additionalImportRules, ...importRuleCollection];
      jest.spyOn(importRuleService, 'addImportRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ importMapping });
      comp.ngOnInit();

      expect(importRuleService.query).toHaveBeenCalled();
      expect(importRuleService.addImportRuleToCollectionIfMissing).toHaveBeenCalledWith(
        importRuleCollection,
        ...additionalImportRules.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.importRulesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const importMapping: IImportMapping = { id: 3649 };
      const rule: IImportRule = { id: 3928 };
      importMapping.rule = rule;

      activatedRoute.data = of({ importMapping });
      comp.ngOnInit();

      expect(comp.importRulesSharedCollection()).toContainEqual(rule);
      expect(comp.importMapping).toEqual(importMapping);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportMapping>>();
      const importMapping = { id: 8044 };
      jest.spyOn(importMappingFormService, 'getImportMapping').mockReturnValue(importMapping);
      jest.spyOn(importMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: importMapping }));
      saveSubject.complete();

      // THEN
      expect(importMappingFormService.getImportMapping).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(importMappingService.update).toHaveBeenCalledWith(expect.objectContaining(importMapping));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportMapping>>();
      const importMapping = { id: 8044 };
      jest.spyOn(importMappingFormService, 'getImportMapping').mockReturnValue({ id: null });
      jest.spyOn(importMappingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importMapping: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: importMapping }));
      saveSubject.complete();

      // THEN
      expect(importMappingFormService.getImportMapping).toHaveBeenCalled();
      expect(importMappingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImportMapping>>();
      const importMapping = { id: 8044 };
      jest.spyOn(importMappingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ importMapping });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(importMappingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImportRule', () => {
      it('should forward to importRuleService', () => {
        const entity = { id: 3928 };
        const entity2 = { id: 22098 };
        jest.spyOn(importRuleService, 'compareImportRule');
        comp.compareImportRule(entity, entity2);
        expect(importRuleService.compareImportRule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
