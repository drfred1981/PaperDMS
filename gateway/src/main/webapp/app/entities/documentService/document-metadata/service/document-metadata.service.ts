import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentMetadata, NewDocumentMetadata } from '../document-metadata.model';

export type PartialUpdateDocumentMetadata = Partial<IDocumentMetadata> & Pick<IDocumentMetadata, 'id'>;

type RestOf<T extends IDocumentMetadata | NewDocumentMetadata> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentMetadata = RestOf<IDocumentMetadata>;

export type NewRestDocumentMetadata = RestOf<NewDocumentMetadata>;

export type PartialUpdateRestDocumentMetadata = RestOf<PartialUpdateDocumentMetadata>;

export type EntityResponseType = HttpResponse<IDocumentMetadata>;
export type EntityArrayResponseType = HttpResponse<IDocumentMetadata[]>;

@Injectable({ providedIn: 'root' })
export class DocumentMetadataService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-metadata', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-metadata/_search', 'documentservice');

  create(documentMetadata: NewDocumentMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentMetadata);
    return this.http
      .post<RestDocumentMetadata>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentMetadata: IDocumentMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentMetadata);
    return this.http
      .put<RestDocumentMetadata>(`${this.resourceUrl}/${this.getDocumentMetadataIdentifier(documentMetadata)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentMetadata: PartialUpdateDocumentMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentMetadata);
    return this.http
      .patch<RestDocumentMetadata>(`${this.resourceUrl}/${this.getDocumentMetadataIdentifier(documentMetadata)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentMetadata[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentMetadata[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentMetadata[]>()], asapScheduler)),
    );
  }

  getDocumentMetadataIdentifier(documentMetadata: Pick<IDocumentMetadata, 'id'>): number {
    return documentMetadata.id;
  }

  compareDocumentMetadata(o1: Pick<IDocumentMetadata, 'id'> | null, o2: Pick<IDocumentMetadata, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentMetadataIdentifier(o1) === this.getDocumentMetadataIdentifier(o2) : o1 === o2;
  }

  addDocumentMetadataToCollectionIfMissing<Type extends Pick<IDocumentMetadata, 'id'>>(
    documentMetadataCollection: Type[],
    ...documentMetadataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentMetadata: Type[] = documentMetadataToCheck.filter(isPresent);
    if (documentMetadata.length > 0) {
      const documentMetadataCollectionIdentifiers = documentMetadataCollection.map(documentMetadataItem =>
        this.getDocumentMetadataIdentifier(documentMetadataItem),
      );
      const documentMetadataToAdd = documentMetadata.filter(documentMetadataItem => {
        const documentMetadataIdentifier = this.getDocumentMetadataIdentifier(documentMetadataItem);
        if (documentMetadataCollectionIdentifiers.includes(documentMetadataIdentifier)) {
          return false;
        }
        documentMetadataCollectionIdentifiers.push(documentMetadataIdentifier);
        return true;
      });
      return [...documentMetadataToAdd, ...documentMetadataCollection];
    }
    return documentMetadataCollection;
  }

  protected convertDateFromClient<T extends IDocumentMetadata | NewDocumentMetadata | PartialUpdateDocumentMetadata>(
    documentMetadata: T,
  ): RestOf<T> {
    return {
      ...documentMetadata,
      createdDate: documentMetadata.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentMetadata: RestDocumentMetadata): IDocumentMetadata {
    return {
      ...restDocumentMetadata,
      createdDate: restDocumentMetadata.createdDate ? dayjs(restDocumentMetadata.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentMetadata>): HttpResponse<IDocumentMetadata> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentMetadata[]>): HttpResponse<IDocumentMetadata[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
