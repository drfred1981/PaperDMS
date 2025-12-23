import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IBookmark } from '../bookmark.model';
import { BookmarkService } from '../service/bookmark.service';

import { BookmarkFormService } from './bookmark-form.service';
import { BookmarkUpdate } from './bookmark-update';

describe('Bookmark Management Update Component', () => {
  let comp: BookmarkUpdate;
  let fixture: ComponentFixture<BookmarkUpdate>;
  let activatedRoute: ActivatedRoute;
  let bookmarkFormService: BookmarkFormService;
  let bookmarkService: BookmarkService;

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

    fixture = TestBed.createComponent(BookmarkUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookmarkFormService = TestBed.inject(BookmarkFormService);
    bookmarkService = TestBed.inject(BookmarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const bookmark: IBookmark = { id: 28441 };

      activatedRoute.data = of({ bookmark });
      comp.ngOnInit();

      expect(comp.bookmark).toEqual(bookmark);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmark>>();
      const bookmark = { id: 18281 };
      jest.spyOn(bookmarkFormService, 'getBookmark').mockReturnValue(bookmark);
      jest.spyOn(bookmarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookmark }));
      saveSubject.complete();

      // THEN
      expect(bookmarkFormService.getBookmark).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookmarkService.update).toHaveBeenCalledWith(expect.objectContaining(bookmark));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmark>>();
      const bookmark = { id: 18281 };
      jest.spyOn(bookmarkFormService, 'getBookmark').mockReturnValue({ id: null });
      jest.spyOn(bookmarkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmark: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookmark }));
      saveSubject.complete();

      // THEN
      expect(bookmarkFormService.getBookmark).toHaveBeenCalled();
      expect(bookmarkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookmark>>();
      const bookmark = { id: 18281 };
      jest.spyOn(bookmarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookmark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookmarkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
