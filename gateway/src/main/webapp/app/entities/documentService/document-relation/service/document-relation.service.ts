import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentRelation, NewDocumentRelation } from '../document-relation.model';

export type PartialUpdateDocumentRelation = Partial<IDocumentRelation> & Pick<IDocumentRelation, 'id'>;

type RestOf<T extends IDocumentRelation | NewDocumentRelation> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentRelation = RestOf<IDocumentRelation>;

export type NewRestDocumentRelation = RestOf<NewDocumentRelation>;

export type PartialUpdateRestDocumentRelation = RestOf<PartialUpdateDocumentRelation>;

export type EntityResponseType = HttpResponse<IDocumentRelation>;
export type EntityArrayResponseType = HttpResponse<IDocumentRelation[]>;

@Injectable({ providedIn: 'root' })
export class DocumentRelationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-relations', 'documentservice');

  create(documentRelation: NewDocumentRelation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentRelation);
    return this.http
      .post<RestDocumentRelation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentRelation: IDocumentRelation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentRelation);
    return this.http
      .put<RestDocumentRelation>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentRelationIdentifier(documentRelation))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentRelation: PartialUpdateDocumentRelation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentRelation);
    return this.http
      .patch<RestDocumentRelation>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentRelationIdentifier(documentRelation))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentRelation>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentRelation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDocumentRelationIdentifier(documentRelation: Pick<IDocumentRelation, 'id'>): number {
    return documentRelation.id;
  }

  compareDocumentRelation(o1: Pick<IDocumentRelation, 'id'> | null, o2: Pick<IDocumentRelation, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentRelationIdentifier(o1) === this.getDocumentRelationIdentifier(o2) : o1 === o2;
  }

  addDocumentRelationToCollectionIfMissing<Type extends Pick<IDocumentRelation, 'id'>>(
    documentRelationCollection: Type[],
    ...documentRelationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentRelations: Type[] = documentRelationsToCheck.filter(isPresent);
    if (documentRelations.length > 0) {
      const documentRelationCollectionIdentifiers = documentRelationCollection.map(documentRelationItem =>
        this.getDocumentRelationIdentifier(documentRelationItem),
      );
      const documentRelationsToAdd = documentRelations.filter(documentRelationItem => {
        const documentRelationIdentifier = this.getDocumentRelationIdentifier(documentRelationItem);
        if (documentRelationCollectionIdentifiers.includes(documentRelationIdentifier)) {
          return false;
        }
        documentRelationCollectionIdentifiers.push(documentRelationIdentifier);
        return true;
      });
      return [...documentRelationsToAdd, ...documentRelationCollection];
    }
    return documentRelationCollection;
  }

  protected convertDateFromClient<T extends IDocumentRelation | NewDocumentRelation | PartialUpdateDocumentRelation>(
    documentRelation: T,
  ): RestOf<T> {
    return {
      ...documentRelation,
      createdDate: documentRelation.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentRelation: RestDocumentRelation): IDocumentRelation {
    return {
      ...restDocumentRelation,
      createdDate: restDocumentRelation.createdDate ? dayjs(restDocumentRelation.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentRelation>): HttpResponse<IDocumentRelation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentRelation[]>): HttpResponse<IDocumentRelation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
