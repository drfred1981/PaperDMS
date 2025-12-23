import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IBookmark, NewBookmark } from '../bookmark.model';

export type PartialUpdateBookmark = Partial<IBookmark> & Pick<IBookmark, 'id'>;

type RestOf<T extends IBookmark | NewBookmark> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestBookmark = RestOf<IBookmark>;

export type NewRestBookmark = RestOf<NewBookmark>;

export type PartialUpdateRestBookmark = RestOf<PartialUpdateBookmark>;

export type EntityResponseType = HttpResponse<IBookmark>;
export type EntityArrayResponseType = HttpResponse<IBookmark[]>;

@Injectable({ providedIn: 'root' })
export class BookmarkService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bookmarks', 'documentservice');

  create(bookmark: NewBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookmark);
    return this.http
      .post<RestBookmark>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bookmark: IBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookmark);
    return this.http
      .put<RestBookmark>(`${this.resourceUrl}/${encodeURIComponent(this.getBookmarkIdentifier(bookmark))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bookmark: PartialUpdateBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookmark);
    return this.http
      .patch<RestBookmark>(`${this.resourceUrl}/${encodeURIComponent(this.getBookmarkIdentifier(bookmark))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBookmark>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBookmark[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getBookmarkIdentifier(bookmark: Pick<IBookmark, 'id'>): number {
    return bookmark.id;
  }

  compareBookmark(o1: Pick<IBookmark, 'id'> | null, o2: Pick<IBookmark, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookmarkIdentifier(o1) === this.getBookmarkIdentifier(o2) : o1 === o2;
  }

  addBookmarkToCollectionIfMissing<Type extends Pick<IBookmark, 'id'>>(
    bookmarkCollection: Type[],
    ...bookmarksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookmarks: Type[] = bookmarksToCheck.filter(isPresent);
    if (bookmarks.length > 0) {
      const bookmarkCollectionIdentifiers = bookmarkCollection.map(bookmarkItem => this.getBookmarkIdentifier(bookmarkItem));
      const bookmarksToAdd = bookmarks.filter(bookmarkItem => {
        const bookmarkIdentifier = this.getBookmarkIdentifier(bookmarkItem);
        if (bookmarkCollectionIdentifiers.includes(bookmarkIdentifier)) {
          return false;
        }
        bookmarkCollectionIdentifiers.push(bookmarkIdentifier);
        return true;
      });
      return [...bookmarksToAdd, ...bookmarkCollection];
    }
    return bookmarkCollection;
  }

  protected convertDateFromClient<T extends IBookmark | NewBookmark | PartialUpdateBookmark>(bookmark: T): RestOf<T> {
    return {
      ...bookmark,
      createdDate: bookmark.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBookmark: RestBookmark): IBookmark {
    return {
      ...restBookmark,
      createdDate: restBookmark.createdDate ? dayjs(restBookmark.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBookmark>): HttpResponse<IBookmark> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBookmark[]>): HttpResponse<IBookmark[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
