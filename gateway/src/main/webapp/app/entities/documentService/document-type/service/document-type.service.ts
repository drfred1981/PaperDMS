import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { IDocumentType, NewDocumentType } from '../document-type.model';

export type PartialUpdateDocumentType = Partial<IDocumentType> & Pick<IDocumentType, 'id'>;

type RestOf<T extends IDocumentType | NewDocumentType> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentType = RestOf<IDocumentType>;

export type NewRestDocumentType = RestOf<NewDocumentType>;

export type PartialUpdateRestDocumentType = RestOf<PartialUpdateDocumentType>;

export type EntityResponseType = HttpResponse<IDocumentType>;
export type EntityArrayResponseType = HttpResponse<IDocumentType[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-types', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-types/_search', 'documentservice');

  create(documentType: NewDocumentType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentType);
    return this.http
      .post<RestDocumentType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentType: IDocumentType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentType);
    return this.http
      .put<RestDocumentType>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentTypeIdentifier(documentType))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentType: PartialUpdateDocumentType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentType);
    return this.http
      .patch<RestDocumentType>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentTypeIdentifier(documentType))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentType>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentType[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentType[]>()], asapScheduler)),
    );
  }

  getDocumentTypeIdentifier(documentType: Pick<IDocumentType, 'id'>): number {
    return documentType.id;
  }

  compareDocumentType(o1: Pick<IDocumentType, 'id'> | null, o2: Pick<IDocumentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentTypeIdentifier(o1) === this.getDocumentTypeIdentifier(o2) : o1 === o2;
  }

  addDocumentTypeToCollectionIfMissing<Type extends Pick<IDocumentType, 'id'>>(
    documentTypeCollection: Type[],
    ...documentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentTypes: Type[] = documentTypesToCheck.filter(isPresent);
    if (documentTypes.length > 0) {
      const documentTypeCollectionIdentifiers = documentTypeCollection.map(documentTypeItem =>
        this.getDocumentTypeIdentifier(documentTypeItem),
      );
      const documentTypesToAdd = documentTypes.filter(documentTypeItem => {
        const documentTypeIdentifier = this.getDocumentTypeIdentifier(documentTypeItem);
        if (documentTypeCollectionIdentifiers.includes(documentTypeIdentifier)) {
          return false;
        }
        documentTypeCollectionIdentifiers.push(documentTypeIdentifier);
        return true;
      });
      return [...documentTypesToAdd, ...documentTypeCollection];
    }
    return documentTypeCollection;
  }

  protected convertDateFromClient<T extends IDocumentType | NewDocumentType | PartialUpdateDocumentType>(documentType: T): RestOf<T> {
    return {
      ...documentType,
      createdDate: documentType.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentType: RestDocumentType): IDocumentType {
    return {
      ...restDocumentType,
      createdDate: restDocumentType.createdDate ? dayjs(restDocumentType.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentType>): HttpResponse<IDocumentType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentType[]>): HttpResponse<IDocumentType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
