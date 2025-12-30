import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentTypeField, NewDocumentTypeField } from '../document-type-field.model';

export type PartialUpdateDocumentTypeField = Partial<IDocumentTypeField> & Pick<IDocumentTypeField, 'id'>;

type RestOf<T extends IDocumentTypeField | NewDocumentTypeField> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentTypeField = RestOf<IDocumentTypeField>;

export type NewRestDocumentTypeField = RestOf<NewDocumentTypeField>;

export type PartialUpdateRestDocumentTypeField = RestOf<PartialUpdateDocumentTypeField>;

export type EntityResponseType = HttpResponse<IDocumentTypeField>;
export type EntityArrayResponseType = HttpResponse<IDocumentTypeField[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTypeFieldService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-type-fields', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-type-fields/_search', 'documentservice');

  create(documentTypeField: NewDocumentTypeField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTypeField);
    return this.http
      .post<RestDocumentTypeField>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentTypeField: IDocumentTypeField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTypeField);
    return this.http
      .put<RestDocumentTypeField>(`${this.resourceUrl}/${this.getDocumentTypeFieldIdentifier(documentTypeField)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentTypeField: PartialUpdateDocumentTypeField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTypeField);
    return this.http
      .patch<RestDocumentTypeField>(`${this.resourceUrl}/${this.getDocumentTypeFieldIdentifier(documentTypeField)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentTypeField>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentTypeField[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentTypeField[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentTypeField[]>()], asapScheduler)),
    );
  }

  getDocumentTypeFieldIdentifier(documentTypeField: Pick<IDocumentTypeField, 'id'>): number {
    return documentTypeField.id;
  }

  compareDocumentTypeField(o1: Pick<IDocumentTypeField, 'id'> | null, o2: Pick<IDocumentTypeField, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentTypeFieldIdentifier(o1) === this.getDocumentTypeFieldIdentifier(o2) : o1 === o2;
  }

  addDocumentTypeFieldToCollectionIfMissing<Type extends Pick<IDocumentTypeField, 'id'>>(
    documentTypeFieldCollection: Type[],
    ...documentTypeFieldsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentTypeFields: Type[] = documentTypeFieldsToCheck.filter(isPresent);
    if (documentTypeFields.length > 0) {
      const documentTypeFieldCollectionIdentifiers = documentTypeFieldCollection.map(documentTypeFieldItem =>
        this.getDocumentTypeFieldIdentifier(documentTypeFieldItem),
      );
      const documentTypeFieldsToAdd = documentTypeFields.filter(documentTypeFieldItem => {
        const documentTypeFieldIdentifier = this.getDocumentTypeFieldIdentifier(documentTypeFieldItem);
        if (documentTypeFieldCollectionIdentifiers.includes(documentTypeFieldIdentifier)) {
          return false;
        }
        documentTypeFieldCollectionIdentifiers.push(documentTypeFieldIdentifier);
        return true;
      });
      return [...documentTypeFieldsToAdd, ...documentTypeFieldCollection];
    }
    return documentTypeFieldCollection;
  }

  protected convertDateFromClient<T extends IDocumentTypeField | NewDocumentTypeField | PartialUpdateDocumentTypeField>(
    documentTypeField: T,
  ): RestOf<T> {
    return {
      ...documentTypeField,
      createdDate: documentTypeField.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentTypeField: RestDocumentTypeField): IDocumentTypeField {
    return {
      ...restDocumentTypeField,
      createdDate: restDocumentTypeField.createdDate ? dayjs(restDocumentTypeField.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentTypeField>): HttpResponse<IDocumentTypeField> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentTypeField[]>): HttpResponse<IDocumentTypeField[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
