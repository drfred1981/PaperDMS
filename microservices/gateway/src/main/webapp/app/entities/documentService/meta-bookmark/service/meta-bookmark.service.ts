import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaBookmark, NewMetaBookmark } from '../meta-bookmark.model';

export type PartialUpdateMetaBookmark = Partial<IMetaBookmark> & Pick<IMetaBookmark, 'id'>;

type RestOf<T extends IMetaBookmark | NewMetaBookmark> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaBookmark = RestOf<IMetaBookmark>;

export type NewRestMetaBookmark = RestOf<NewMetaBookmark>;

export type PartialUpdateRestMetaBookmark = RestOf<PartialUpdateMetaBookmark>;

export type EntityResponseType = HttpResponse<IMetaBookmark>;
export type EntityArrayResponseType = HttpResponse<IMetaBookmark[]>;

@Injectable({ providedIn: 'root' })
export class MetaBookmarkService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-bookmarks', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-bookmarks/_search', 'documentservice');

  create(metaBookmark: NewMetaBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaBookmark);
    return this.http
      .post<RestMetaBookmark>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaBookmark: IMetaBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaBookmark);
    return this.http
      .put<RestMetaBookmark>(`${this.resourceUrl}/${this.getMetaBookmarkIdentifier(metaBookmark)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaBookmark: PartialUpdateMetaBookmark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaBookmark);
    return this.http
      .patch<RestMetaBookmark>(`${this.resourceUrl}/${this.getMetaBookmarkIdentifier(metaBookmark)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaBookmark>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaBookmark[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaBookmark[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaBookmark[]>()], asapScheduler)),
    );
  }

  getMetaBookmarkIdentifier(metaBookmark: Pick<IMetaBookmark, 'id'>): number {
    return metaBookmark.id;
  }

  compareMetaBookmark(o1: Pick<IMetaBookmark, 'id'> | null, o2: Pick<IMetaBookmark, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaBookmarkIdentifier(o1) === this.getMetaBookmarkIdentifier(o2) : o1 === o2;
  }

  addMetaBookmarkToCollectionIfMissing<Type extends Pick<IMetaBookmark, 'id'>>(
    metaBookmarkCollection: Type[],
    ...metaBookmarksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaBookmarks: Type[] = metaBookmarksToCheck.filter(isPresent);
    if (metaBookmarks.length > 0) {
      const metaBookmarkCollectionIdentifiers = metaBookmarkCollection.map(metaBookmarkItem =>
        this.getMetaBookmarkIdentifier(metaBookmarkItem),
      );
      const metaBookmarksToAdd = metaBookmarks.filter(metaBookmarkItem => {
        const metaBookmarkIdentifier = this.getMetaBookmarkIdentifier(metaBookmarkItem);
        if (metaBookmarkCollectionIdentifiers.includes(metaBookmarkIdentifier)) {
          return false;
        }
        metaBookmarkCollectionIdentifiers.push(metaBookmarkIdentifier);
        return true;
      });
      return [...metaBookmarksToAdd, ...metaBookmarkCollection];
    }
    return metaBookmarkCollection;
  }

  protected convertDateFromClient<T extends IMetaBookmark | NewMetaBookmark | PartialUpdateMetaBookmark>(metaBookmark: T): RestOf<T> {
    return {
      ...metaBookmark,
      createdDate: metaBookmark.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaBookmark: RestMetaBookmark): IMetaBookmark {
    return {
      ...restMetaBookmark,
      createdDate: restMetaBookmark.createdDate ? dayjs(restMetaBookmark.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaBookmark>): HttpResponse<IMetaBookmark> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaBookmark[]>): HttpResponse<IMetaBookmark[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
