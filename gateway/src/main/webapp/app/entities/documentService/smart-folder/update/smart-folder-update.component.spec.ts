import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SmartFolderService } from '../service/smart-folder.service';
import { ISmartFolder } from '../smart-folder.model';
import { SmartFolderFormService } from './smart-folder-form.service';

import { SmartFolderUpdateComponent } from './smart-folder-update.component';

describe('SmartFolder Management Update Component', () => {
  let comp: SmartFolderUpdateComponent;
  let fixture: ComponentFixture<SmartFolderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let smartFolderFormService: SmartFolderFormService;
  let smartFolderService: SmartFolderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SmartFolderUpdateComponent],
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
      .overrideTemplate(SmartFolderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SmartFolderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    smartFolderFormService = TestBed.inject(SmartFolderFormService);
    smartFolderService = TestBed.inject(SmartFolderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const smartFolder: ISmartFolder = { id: 19985 };

      activatedRoute.data = of({ smartFolder });
      comp.ngOnInit();

      expect(comp.smartFolder).toEqual(smartFolder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISmartFolder>>();
      const smartFolder = { id: 14548 };
      jest.spyOn(smartFolderFormService, 'getSmartFolder').mockReturnValue(smartFolder);
      jest.spyOn(smartFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ smartFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: smartFolder }));
      saveSubject.complete();

      // THEN
      expect(smartFolderFormService.getSmartFolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(smartFolderService.update).toHaveBeenCalledWith(expect.objectContaining(smartFolder));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISmartFolder>>();
      const smartFolder = { id: 14548 };
      jest.spyOn(smartFolderFormService, 'getSmartFolder').mockReturnValue({ id: null });
      jest.spyOn(smartFolderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ smartFolder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: smartFolder }));
      saveSubject.complete();

      // THEN
      expect(smartFolderFormService.getSmartFolder).toHaveBeenCalled();
      expect(smartFolderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISmartFolder>>();
      const smartFolder = { id: 14548 };
      jest.spyOn(smartFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ smartFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(smartFolderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
