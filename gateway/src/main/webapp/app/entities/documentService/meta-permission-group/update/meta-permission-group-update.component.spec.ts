import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaPermissionGroupService } from '../service/meta-permission-group.service';
import { IMetaPermissionGroup } from '../meta-permission-group.model';
import { MetaPermissionGroupFormService } from './meta-permission-group-form.service';

import { MetaPermissionGroupUpdateComponent } from './meta-permission-group-update.component';

describe('MetaPermissionGroup Management Update Component', () => {
  let comp: MetaPermissionGroupUpdateComponent;
  let fixture: ComponentFixture<MetaPermissionGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaPermissionGroupFormService: MetaPermissionGroupFormService;
  let metaPermissionGroupService: MetaPermissionGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaPermissionGroupUpdateComponent],
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
      .overrideTemplate(MetaPermissionGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaPermissionGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaPermissionGroupFormService = TestBed.inject(MetaPermissionGroupFormService);
    metaPermissionGroupService = TestBed.inject(MetaPermissionGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const metaPermissionGroup: IMetaPermissionGroup = { id: 27974 };

      activatedRoute.data = of({ metaPermissionGroup });
      comp.ngOnInit();

      expect(comp.metaPermissionGroup).toEqual(metaPermissionGroup);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaPermissionGroup>>();
      const metaPermissionGroup = { id: 4839 };
      jest.spyOn(metaPermissionGroupFormService, 'getMetaPermissionGroup').mockReturnValue(metaPermissionGroup);
      jest.spyOn(metaPermissionGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaPermissionGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaPermissionGroup }));
      saveSubject.complete();

      // THEN
      expect(metaPermissionGroupFormService.getMetaPermissionGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaPermissionGroupService.update).toHaveBeenCalledWith(expect.objectContaining(metaPermissionGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaPermissionGroup>>();
      const metaPermissionGroup = { id: 4839 };
      jest.spyOn(metaPermissionGroupFormService, 'getMetaPermissionGroup').mockReturnValue({ id: null });
      jest.spyOn(metaPermissionGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaPermissionGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaPermissionGroup }));
      saveSubject.complete();

      // THEN
      expect(metaPermissionGroupFormService.getMetaPermissionGroup).toHaveBeenCalled();
      expect(metaPermissionGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaPermissionGroup>>();
      const metaPermissionGroup = { id: 4839 };
      jest.spyOn(metaPermissionGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaPermissionGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaPermissionGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
