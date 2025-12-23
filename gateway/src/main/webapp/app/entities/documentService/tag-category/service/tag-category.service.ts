import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { ITagCategory, NewTagCategory } from '../tag-category.model';

export type PartialUpdateTagCategory = Partial<ITagCategory> & Pick<ITagCategory, 'id'>;

type RestOf<T extends ITagCategory | NewTagCategory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestTagCategory = RestOf<ITagCategory>;

export type NewRestTagCategory = RestOf<NewTagCategory>;

export type PartialUpdateRestTagCategory = RestOf<PartialUpdateTagCategory>;

export type EntityResponseType = HttpResponse<ITagCategory>;
export type EntityArrayResponseType = HttpResponse<ITagCategory[]>;

@Injectable({ providedIn: 'root' })
export class TagCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tag-categories', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tag-categories/_search', 'documentservice');

  create(tagCategory: NewTagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagCategory);
    return this.http
      .post<RestTagCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tagCategory: ITagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagCategory);
    return this.http
      .put<RestTagCategory>(`${this.resourceUrl}/${encodeURIComponent(this.getTagCategoryIdentifier(tagCategory))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tagCategory: PartialUpdateTagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tagCategory);
    return this.http
      .patch<RestTagCategory>(`${this.resourceUrl}/${encodeURIComponent(this.getTagCategoryIdentifier(tagCategory))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTagCategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTagCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTagCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ITagCategory[]>()], asapScheduler)),
    );
  }

  getTagCategoryIdentifier(tagCategory: Pick<ITagCategory, 'id'>): number {
    return tagCategory.id;
  }

  compareTagCategory(o1: Pick<ITagCategory, 'id'> | null, o2: Pick<ITagCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getTagCategoryIdentifier(o1) === this.getTagCategoryIdentifier(o2) : o1 === o2;
  }

  addTagCategoryToCollectionIfMissing<Type extends Pick<ITagCategory, 'id'>>(
    tagCategoryCollection: Type[],
    ...tagCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tagCategories: Type[] = tagCategoriesToCheck.filter(isPresent);
    if (tagCategories.length > 0) {
      const tagCategoryCollectionIdentifiers = tagCategoryCollection.map(tagCategoryItem => this.getTagCategoryIdentifier(tagCategoryItem));
      const tagCategoriesToAdd = tagCategories.filter(tagCategoryItem => {
        const tagCategoryIdentifier = this.getTagCategoryIdentifier(tagCategoryItem);
        if (tagCategoryCollectionIdentifiers.includes(tagCategoryIdentifier)) {
          return false;
        }
        tagCategoryCollectionIdentifiers.push(tagCategoryIdentifier);
        return true;
      });
      return [...tagCategoriesToAdd, ...tagCategoryCollection];
    }
    return tagCategoryCollection;
  }

  protected convertDateFromClient<T extends ITagCategory | NewTagCategory | PartialUpdateTagCategory>(tagCategory: T): RestOf<T> {
    return {
      ...tagCategory,
      createdDate: tagCategory.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTagCategory: RestTagCategory): ITagCategory {
    return {
      ...restTagCategory,
      createdDate: restTagCategory.createdDate ? dayjs(restTagCategory.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTagCategory>): HttpResponse<ITagCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTagCategory[]>): HttpResponse<ITagCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
