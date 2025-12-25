import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ExtractedFieldService } from '../service/extracted-field.service';
import { IExtractedField } from '../extracted-field.model';
import { ExtractedFieldFormService } from './extracted-field-form.service';

import { ExtractedFieldUpdateComponent } from './extracted-field-update.component';

describe('ExtractedField Management Update Component', () => {
  let comp: ExtractedFieldUpdateComponent;
  let fixture: ComponentFixture<ExtractedFieldUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let extractedFieldFormService: ExtractedFieldFormService;
  let extractedFieldService: ExtractedFieldService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExtractedFieldUpdateComponent],
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
      .overrideTemplate(ExtractedFieldUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExtractedFieldUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extractedFieldFormService = TestBed.inject(ExtractedFieldFormService);
    extractedFieldService = TestBed.inject(ExtractedFieldService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const extractedField: IExtractedField = { id: 8866 };

      activatedRoute.data = of({ extractedField });
      comp.ngOnInit();

      expect(comp.extractedField).toEqual(extractedField);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedField>>();
      const extractedField = { id: 31442 };
      jest.spyOn(extractedFieldFormService, 'getExtractedField').mockReturnValue(extractedField);
      jest.spyOn(extractedFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extractedField }));
      saveSubject.complete();

      // THEN
      expect(extractedFieldFormService.getExtractedField).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extractedFieldService.update).toHaveBeenCalledWith(expect.objectContaining(extractedField));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedField>>();
      const extractedField = { id: 31442 };
      jest.spyOn(extractedFieldFormService, 'getExtractedField').mockReturnValue({ id: null });
      jest.spyOn(extractedFieldService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedField: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extractedField }));
      saveSubject.complete();

      // THEN
      expect(extractedFieldFormService.getExtractedField).toHaveBeenCalled();
      expect(extractedFieldService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedField>>();
      const extractedField = { id: 31442 };
      jest.spyOn(extractedFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extractedFieldService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
