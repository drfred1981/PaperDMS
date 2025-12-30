import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaBookmarkService } from '../service/meta-bookmark.service';
import { IMetaBookmark } from '../meta-bookmark.model';
import { MetaBookmarkFormService } from './meta-bookmark-form.service';

import { MetaBookmarkUpdateComponent } from './meta-bookmark-update.component';

describe('MetaBookmark Management Update Component', () => {
  let comp: MetaBookmarkUpdateComponent;
  let fixture: ComponentFixture<MetaBookmarkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaBookmarkFormService: MetaBookmarkFormService;
  let metaBookmarkService: MetaBookmarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaBookmarkUpdateComponent],
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
      .overrideTemplate(MetaBookmarkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaBookmarkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaBookmarkFormService = TestBed.inject(MetaBookmarkFormService);
    metaBookmarkService = TestBed.inject(MetaBookmarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const metaBookmark: IMetaBookmark = { id: 14115 };

      activatedRoute.data = of({ metaBookmark });
      comp.ngOnInit();

      expect(comp.metaBookmark).toEqual(metaBookmark);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaBookmark>>();
      const metaBookmark = { id: 12947 };
      jest.spyOn(metaBookmarkFormService, 'getMetaBookmark').mockReturnValue(metaBookmark);
      jest.spyOn(metaBookmarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaBookmark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaBookmark }));
      saveSubject.complete();

      // THEN
      expect(metaBookmarkFormService.getMetaBookmark).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaBookmarkService.update).toHaveBeenCalledWith(expect.objectContaining(metaBookmark));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaBookmark>>();
      const metaBookmark = { id: 12947 };
      jest.spyOn(metaBookmarkFormService, 'getMetaBookmark').mockReturnValue({ id: null });
      jest.spyOn(metaBookmarkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaBookmark: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaBookmark }));
      saveSubject.complete();

      // THEN
      expect(metaBookmarkFormService.getMetaBookmark).toHaveBeenCalled();
      expect(metaBookmarkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaBookmark>>();
      const metaBookmark = { id: 12947 };
      jest.spyOn(metaBookmarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaBookmark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaBookmarkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
