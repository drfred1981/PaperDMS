import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IManual } from '../manual.model';
import { ManualService } from '../service/manual.service';

import { ManualFormService } from './manual-form.service';
import { ManualUpdate } from './manual-update';

describe('Manual Management Update Component', () => {
  let comp: ManualUpdate;
  let fixture: ComponentFixture<ManualUpdate>;
  let activatedRoute: ActivatedRoute;
  let manualFormService: ManualFormService;
  let manualService: ManualService;

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

    fixture = TestBed.createComponent(ManualUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualFormService = TestBed.inject(ManualFormService);
    manualService = TestBed.inject(ManualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const manual: IManual = { id: 17096 };

      activatedRoute.data = of({ manual });
      comp.ngOnInit();

      expect(comp.manual).toEqual(manual);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManual>>();
      const manual = { id: 16259 };
      jest.spyOn(manualFormService, 'getManual').mockReturnValue(manual);
      jest.spyOn(manualService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manual }));
      saveSubject.complete();

      // THEN
      expect(manualFormService.getManual).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualService.update).toHaveBeenCalledWith(expect.objectContaining(manual));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManual>>();
      const manual = { id: 16259 };
      jest.spyOn(manualFormService, 'getManual').mockReturnValue({ id: null });
      jest.spyOn(manualService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manual: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manual }));
      saveSubject.complete();

      // THEN
      expect(manualFormService.getManual).toHaveBeenCalled();
      expect(manualService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManual>>();
      const manual = { id: 16259 };
      jest.spyOn(manualService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
