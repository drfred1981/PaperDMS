import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentExtractedField, NewDocumentExtractedField } from '../document-extracted-field.model';

export type PartialUpdateDocumentExtractedField = Partial<IDocumentExtractedField> & Pick<IDocumentExtractedField, 'id'>;

type RestOf<T extends IDocumentExtractedField | NewDocumentExtractedField> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

export type RestDocumentExtractedField = RestOf<IDocumentExtractedField>;

export type NewRestDocumentExtractedField = RestOf<NewDocumentExtractedField>;

export type PartialUpdateRestDocumentExtractedField = RestOf<PartialUpdateDocumentExtractedField>;

export type EntityResponseType = HttpResponse<IDocumentExtractedField>;
export type EntityArrayResponseType = HttpResponse<IDocumentExtractedField[]>;

@Injectable({ providedIn: 'root' })
export class DocumentExtractedFieldService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-extracted-fields', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-extracted-fields/_search', 'documentservice');

  create(documentExtractedField: NewDocumentExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentExtractedField);
    return this.http
      .post<RestDocumentExtractedField>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentExtractedField: IDocumentExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentExtractedField);
    return this.http
      .put<RestDocumentExtractedField>(`${this.resourceUrl}/${this.getDocumentExtractedFieldIdentifier(documentExtractedField)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentExtractedField: PartialUpdateDocumentExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentExtractedField);
    return this.http
      .patch<RestDocumentExtractedField>(`${this.resourceUrl}/${this.getDocumentExtractedFieldIdentifier(documentExtractedField)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentExtractedField>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentExtractedField[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentExtractedField[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentExtractedField[]>()], asapScheduler)),
    );
  }

  getDocumentExtractedFieldIdentifier(documentExtractedField: Pick<IDocumentExtractedField, 'id'>): number {
    return documentExtractedField.id;
  }

  compareDocumentExtractedField(o1: Pick<IDocumentExtractedField, 'id'> | null, o2: Pick<IDocumentExtractedField, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentExtractedFieldIdentifier(o1) === this.getDocumentExtractedFieldIdentifier(o2) : o1 === o2;
  }

  addDocumentExtractedFieldToCollectionIfMissing<Type extends Pick<IDocumentExtractedField, 'id'>>(
    documentExtractedFieldCollection: Type[],
    ...documentExtractedFieldsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentExtractedFields: Type[] = documentExtractedFieldsToCheck.filter(isPresent);
    if (documentExtractedFields.length > 0) {
      const documentExtractedFieldCollectionIdentifiers = documentExtractedFieldCollection.map(documentExtractedFieldItem =>
        this.getDocumentExtractedFieldIdentifier(documentExtractedFieldItem),
      );
      const documentExtractedFieldsToAdd = documentExtractedFields.filter(documentExtractedFieldItem => {
        const documentExtractedFieldIdentifier = this.getDocumentExtractedFieldIdentifier(documentExtractedFieldItem);
        if (documentExtractedFieldCollectionIdentifiers.includes(documentExtractedFieldIdentifier)) {
          return false;
        }
        documentExtractedFieldCollectionIdentifiers.push(documentExtractedFieldIdentifier);
        return true;
      });
      return [...documentExtractedFieldsToAdd, ...documentExtractedFieldCollection];
    }
    return documentExtractedFieldCollection;
  }

  protected convertDateFromClient<T extends IDocumentExtractedField | NewDocumentExtractedField | PartialUpdateDocumentExtractedField>(
    documentExtractedField: T,
  ): RestOf<T> {
    return {
      ...documentExtractedField,
      extractedDate: documentExtractedField.extractedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentExtractedField: RestDocumentExtractedField): IDocumentExtractedField {
    return {
      ...restDocumentExtractedField,
      extractedDate: restDocumentExtractedField.extractedDate ? dayjs(restDocumentExtractedField.extractedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentExtractedField>): HttpResponse<IDocumentExtractedField> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentExtractedField[]>): HttpResponse<IDocumentExtractedField[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
