import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaTag, NewMetaTag } from '../meta-tag.model';

export type PartialUpdateMetaTag = Partial<IMetaTag> & Pick<IMetaTag, 'id'>;

type RestOf<T extends IMetaTag | NewMetaTag> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaTag = RestOf<IMetaTag>;

export type NewRestMetaTag = RestOf<NewMetaTag>;

export type PartialUpdateRestMetaTag = RestOf<PartialUpdateMetaTag>;

export type EntityResponseType = HttpResponse<IMetaTag>;
export type EntityArrayResponseType = HttpResponse<IMetaTag[]>;

@Injectable({ providedIn: 'root' })
export class MetaTagService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-tags', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-tags/_search', 'documentservice');

  create(metaTag: NewMetaTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaTag);
    return this.http
      .post<RestMetaTag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaTag: IMetaTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaTag);
    return this.http
      .put<RestMetaTag>(`${this.resourceUrl}/${this.getMetaTagIdentifier(metaTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaTag: PartialUpdateMetaTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaTag);
    return this.http
      .patch<RestMetaTag>(`${this.resourceUrl}/${this.getMetaTagIdentifier(metaTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaTag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaTag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaTag[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaTag[]>()], asapScheduler)),
    );
  }

  getMetaTagIdentifier(metaTag: Pick<IMetaTag, 'id'>): number {
    return metaTag.id;
  }

  compareMetaTag(o1: Pick<IMetaTag, 'id'> | null, o2: Pick<IMetaTag, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaTagIdentifier(o1) === this.getMetaTagIdentifier(o2) : o1 === o2;
  }

  addMetaTagToCollectionIfMissing<Type extends Pick<IMetaTag, 'id'>>(
    metaTagCollection: Type[],
    ...metaTagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaTags: Type[] = metaTagsToCheck.filter(isPresent);
    if (metaTags.length > 0) {
      const metaTagCollectionIdentifiers = metaTagCollection.map(metaTagItem => this.getMetaTagIdentifier(metaTagItem));
      const metaTagsToAdd = metaTags.filter(metaTagItem => {
        const metaTagIdentifier = this.getMetaTagIdentifier(metaTagItem);
        if (metaTagCollectionIdentifiers.includes(metaTagIdentifier)) {
          return false;
        }
        metaTagCollectionIdentifiers.push(metaTagIdentifier);
        return true;
      });
      return [...metaTagsToAdd, ...metaTagCollection];
    }
    return metaTagCollection;
  }

  protected convertDateFromClient<T extends IMetaTag | NewMetaTag | PartialUpdateMetaTag>(metaTag: T): RestOf<T> {
    return {
      ...metaTag,
      createdDate: metaTag.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaTag: RestMetaTag): IMetaTag {
    return {
      ...restMetaTag,
      createdDate: restMetaTag.createdDate ? dayjs(restMetaTag.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaTag>): HttpResponse<IMetaTag> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaTag[]>): HttpResponse<IMetaTag[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
