import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaSmartFolderService } from '../service/meta-smart-folder.service';
import { IMetaSmartFolder } from '../meta-smart-folder.model';
import { MetaSmartFolderFormService } from './meta-smart-folder-form.service';

import { MetaSmartFolderUpdateComponent } from './meta-smart-folder-update.component';

describe('MetaSmartFolder Management Update Component', () => {
  let comp: MetaSmartFolderUpdateComponent;
  let fixture: ComponentFixture<MetaSmartFolderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaSmartFolderFormService: MetaSmartFolderFormService;
  let metaSmartFolderService: MetaSmartFolderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaSmartFolderUpdateComponent],
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
      .overrideTemplate(MetaSmartFolderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaSmartFolderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaSmartFolderFormService = TestBed.inject(MetaSmartFolderFormService);
    metaSmartFolderService = TestBed.inject(MetaSmartFolderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const metaSmartFolder: IMetaSmartFolder = { id: 1010 };

      activatedRoute.data = of({ metaSmartFolder });
      comp.ngOnInit();

      expect(comp.metaSmartFolder).toEqual(metaSmartFolder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSmartFolder>>();
      const metaSmartFolder = { id: 10831 };
      jest.spyOn(metaSmartFolderFormService, 'getMetaSmartFolder').mockReturnValue(metaSmartFolder);
      jest.spyOn(metaSmartFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSmartFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaSmartFolder }));
      saveSubject.complete();

      // THEN
      expect(metaSmartFolderFormService.getMetaSmartFolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaSmartFolderService.update).toHaveBeenCalledWith(expect.objectContaining(metaSmartFolder));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSmartFolder>>();
      const metaSmartFolder = { id: 10831 };
      jest.spyOn(metaSmartFolderFormService, 'getMetaSmartFolder').mockReturnValue({ id: null });
      jest.spyOn(metaSmartFolderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSmartFolder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaSmartFolder }));
      saveSubject.complete();

      // THEN
      expect(metaSmartFolderFormService.getMetaSmartFolder).toHaveBeenCalled();
      expect(metaSmartFolderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaSmartFolder>>();
      const metaSmartFolder = { id: 10831 };
      jest.spyOn(metaSmartFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaSmartFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaSmartFolderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
