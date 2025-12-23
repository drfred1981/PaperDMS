import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentSimilarity, NewDocumentSimilarity } from '../document-similarity.model';

export type PartialUpdateDocumentSimilarity = Partial<IDocumentSimilarity> & Pick<IDocumentSimilarity, 'id'>;

type RestOf<T extends IDocumentSimilarity | NewDocumentSimilarity> = Omit<T, 'computedDate' | 'reviewedDate'> & {
  computedDate?: string | null;
  reviewedDate?: string | null;
};

export type RestDocumentSimilarity = RestOf<IDocumentSimilarity>;

export type NewRestDocumentSimilarity = RestOf<NewDocumentSimilarity>;

export type PartialUpdateRestDocumentSimilarity = RestOf<PartialUpdateDocumentSimilarity>;

export type EntityResponseType = HttpResponse<IDocumentSimilarity>;
export type EntityArrayResponseType = HttpResponse<IDocumentSimilarity[]>;

@Injectable({ providedIn: 'root' })
export class DocumentSimilarityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-similarities', 'similarityservice');

  create(documentSimilarity: NewDocumentSimilarity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSimilarity);
    return this.http
      .post<RestDocumentSimilarity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentSimilarity: IDocumentSimilarity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSimilarity);
    return this.http
      .put<RestDocumentSimilarity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentSimilarityIdentifier(documentSimilarity))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentSimilarity: PartialUpdateDocumentSimilarity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentSimilarity);
    return this.http
      .patch<RestDocumentSimilarity>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentSimilarityIdentifier(documentSimilarity))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentSimilarity>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentSimilarity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDocumentSimilarityIdentifier(documentSimilarity: Pick<IDocumentSimilarity, 'id'>): number {
    return documentSimilarity.id;
  }

  compareDocumentSimilarity(o1: Pick<IDocumentSimilarity, 'id'> | null, o2: Pick<IDocumentSimilarity, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentSimilarityIdentifier(o1) === this.getDocumentSimilarityIdentifier(o2) : o1 === o2;
  }

  addDocumentSimilarityToCollectionIfMissing<Type extends Pick<IDocumentSimilarity, 'id'>>(
    documentSimilarityCollection: Type[],
    ...documentSimilaritiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentSimilarities: Type[] = documentSimilaritiesToCheck.filter(isPresent);
    if (documentSimilarities.length > 0) {
      const documentSimilarityCollectionIdentifiers = documentSimilarityCollection.map(documentSimilarityItem =>
        this.getDocumentSimilarityIdentifier(documentSimilarityItem),
      );
      const documentSimilaritiesToAdd = documentSimilarities.filter(documentSimilarityItem => {
        const documentSimilarityIdentifier = this.getDocumentSimilarityIdentifier(documentSimilarityItem);
        if (documentSimilarityCollectionIdentifiers.includes(documentSimilarityIdentifier)) {
          return false;
        }
        documentSimilarityCollectionIdentifiers.push(documentSimilarityIdentifier);
        return true;
      });
      return [...documentSimilaritiesToAdd, ...documentSimilarityCollection];
    }
    return documentSimilarityCollection;
  }

  protected convertDateFromClient<T extends IDocumentSimilarity | NewDocumentSimilarity | PartialUpdateDocumentSimilarity>(
    documentSimilarity: T,
  ): RestOf<T> {
    return {
      ...documentSimilarity,
      computedDate: documentSimilarity.computedDate?.toJSON() ?? null,
      reviewedDate: documentSimilarity.reviewedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentSimilarity: RestDocumentSimilarity): IDocumentSimilarity {
    return {
      ...restDocumentSimilarity,
      computedDate: restDocumentSimilarity.computedDate ? dayjs(restDocumentSimilarity.computedDate) : undefined,
      reviewedDate: restDocumentSimilarity.reviewedDate ? dayjs(restDocumentSimilarity.reviewedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentSimilarity>): HttpResponse<IDocumentSimilarity> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentSimilarity[]>): HttpResponse<IDocumentSimilarity[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
