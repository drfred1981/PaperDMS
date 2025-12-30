import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaMetaTagCategory, NewMetaMetaTagCategory } from '../meta-meta-tag-category.model';

export type PartialUpdateMetaMetaTagCategory = Partial<IMetaMetaTagCategory> & Pick<IMetaMetaTagCategory, 'id'>;

type RestOf<T extends IMetaMetaTagCategory | NewMetaMetaTagCategory> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaMetaTagCategory = RestOf<IMetaMetaTagCategory>;

export type NewRestMetaMetaTagCategory = RestOf<NewMetaMetaTagCategory>;

export type PartialUpdateRestMetaMetaTagCategory = RestOf<PartialUpdateMetaMetaTagCategory>;

export type EntityResponseType = HttpResponse<IMetaMetaTagCategory>;
export type EntityArrayResponseType = HttpResponse<IMetaMetaTagCategory[]>;

@Injectable({ providedIn: 'root' })
export class MetaMetaTagCategoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-meta-tag-categories', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-meta-tag-categories/_search', 'documentservice');

  create(metaMetaTagCategory: NewMetaMetaTagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaMetaTagCategory);
    return this.http
      .post<RestMetaMetaTagCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaMetaTagCategory: IMetaMetaTagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaMetaTagCategory);
    return this.http
      .put<RestMetaMetaTagCategory>(`${this.resourceUrl}/${this.getMetaMetaTagCategoryIdentifier(metaMetaTagCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaMetaTagCategory: PartialUpdateMetaMetaTagCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaMetaTagCategory);
    return this.http
      .patch<RestMetaMetaTagCategory>(`${this.resourceUrl}/${this.getMetaMetaTagCategoryIdentifier(metaMetaTagCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaMetaTagCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaMetaTagCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaMetaTagCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaMetaTagCategory[]>()], asapScheduler)),
    );
  }

  getMetaMetaTagCategoryIdentifier(metaMetaTagCategory: Pick<IMetaMetaTagCategory, 'id'>): number {
    return metaMetaTagCategory.id;
  }

  compareMetaMetaTagCategory(o1: Pick<IMetaMetaTagCategory, 'id'> | null, o2: Pick<IMetaMetaTagCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaMetaTagCategoryIdentifier(o1) === this.getMetaMetaTagCategoryIdentifier(o2) : o1 === o2;
  }

  addMetaMetaTagCategoryToCollectionIfMissing<Type extends Pick<IMetaMetaTagCategory, 'id'>>(
    metaMetaTagCategoryCollection: Type[],
    ...metaMetaTagCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaMetaTagCategories: Type[] = metaMetaTagCategoriesToCheck.filter(isPresent);
    if (metaMetaTagCategories.length > 0) {
      const metaMetaTagCategoryCollectionIdentifiers = metaMetaTagCategoryCollection.map(metaMetaTagCategoryItem =>
        this.getMetaMetaTagCategoryIdentifier(metaMetaTagCategoryItem),
      );
      const metaMetaTagCategoriesToAdd = metaMetaTagCategories.filter(metaMetaTagCategoryItem => {
        const metaMetaTagCategoryIdentifier = this.getMetaMetaTagCategoryIdentifier(metaMetaTagCategoryItem);
        if (metaMetaTagCategoryCollectionIdentifiers.includes(metaMetaTagCategoryIdentifier)) {
          return false;
        }
        metaMetaTagCategoryCollectionIdentifiers.push(metaMetaTagCategoryIdentifier);
        return true;
      });
      return [...metaMetaTagCategoriesToAdd, ...metaMetaTagCategoryCollection];
    }
    return metaMetaTagCategoryCollection;
  }

  protected convertDateFromClient<T extends IMetaMetaTagCategory | NewMetaMetaTagCategory | PartialUpdateMetaMetaTagCategory>(
    metaMetaTagCategory: T,
  ): RestOf<T> {
    return {
      ...metaMetaTagCategory,
      createdDate: metaMetaTagCategory.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaMetaTagCategory: RestMetaMetaTagCategory): IMetaMetaTagCategory {
    return {
      ...restMetaMetaTagCategory,
      createdDate: restMetaMetaTagCategory.createdDate ? dayjs(restMetaMetaTagCategory.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaMetaTagCategory>): HttpResponse<IMetaMetaTagCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaMetaTagCategory[]>): HttpResponse<IMetaMetaTagCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
