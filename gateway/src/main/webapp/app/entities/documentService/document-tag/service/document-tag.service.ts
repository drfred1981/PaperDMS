import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentTag, NewDocumentTag } from '../document-tag.model';

export type PartialUpdateDocumentTag = Partial<IDocumentTag> & Pick<IDocumentTag, 'id'>;

type RestOf<T extends IDocumentTag | NewDocumentTag> = Omit<T, 'assignedDate'> & {
  assignedDate?: string | null;
};

export type RestDocumentTag = RestOf<IDocumentTag>;

export type NewRestDocumentTag = RestOf<NewDocumentTag>;

export type PartialUpdateRestDocumentTag = RestOf<PartialUpdateDocumentTag>;

export type EntityResponseType = HttpResponse<IDocumentTag>;
export type EntityArrayResponseType = HttpResponse<IDocumentTag[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTagService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-tags', 'documentservice');

  create(documentTag: NewDocumentTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTag);
    return this.http
      .post<RestDocumentTag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentTag: IDocumentTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTag);
    return this.http
      .put<RestDocumentTag>(`${this.resourceUrl}/${this.getDocumentTagIdentifier(documentTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentTag: PartialUpdateDocumentTag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTag);
    return this.http
      .patch<RestDocumentTag>(`${this.resourceUrl}/${this.getDocumentTagIdentifier(documentTag)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentTag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentTag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDocumentTagIdentifier(documentTag: Pick<IDocumentTag, 'id'>): number {
    return documentTag.id;
  }

  compareDocumentTag(o1: Pick<IDocumentTag, 'id'> | null, o2: Pick<IDocumentTag, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentTagIdentifier(o1) === this.getDocumentTagIdentifier(o2) : o1 === o2;
  }

  addDocumentTagToCollectionIfMissing<Type extends Pick<IDocumentTag, 'id'>>(
    documentTagCollection: Type[],
    ...documentTagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentTags: Type[] = documentTagsToCheck.filter(isPresent);
    if (documentTags.length > 0) {
      const documentTagCollectionIdentifiers = documentTagCollection.map(documentTagItem => this.getDocumentTagIdentifier(documentTagItem));
      const documentTagsToAdd = documentTags.filter(documentTagItem => {
        const documentTagIdentifier = this.getDocumentTagIdentifier(documentTagItem);
        if (documentTagCollectionIdentifiers.includes(documentTagIdentifier)) {
          return false;
        }
        documentTagCollectionIdentifiers.push(documentTagIdentifier);
        return true;
      });
      return [...documentTagsToAdd, ...documentTagCollection];
    }
    return documentTagCollection;
  }

  protected convertDateFromClient<T extends IDocumentTag | NewDocumentTag | PartialUpdateDocumentTag>(documentTag: T): RestOf<T> {
    return {
      ...documentTag,
      assignedDate: documentTag.assignedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentTag: RestDocumentTag): IDocumentTag {
    return {
      ...restDocumentTag,
      assignedDate: restDocumentTag.assignedDate ? dayjs(restDocumentTag.assignedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentTag>): HttpResponse<IDocumentTag> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentTag[]>): HttpResponse<IDocumentTag[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
