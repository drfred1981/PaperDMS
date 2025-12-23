import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { IExtractedField, NewExtractedField } from '../extracted-field.model';

export type PartialUpdateExtractedField = Partial<IExtractedField> & Pick<IExtractedField, 'id'>;

type RestOf<T extends IExtractedField | NewExtractedField> = Omit<T, 'extractedDate'> & {
  extractedDate?: string | null;
};

export type RestExtractedField = RestOf<IExtractedField>;

export type NewRestExtractedField = RestOf<NewExtractedField>;

export type PartialUpdateRestExtractedField = RestOf<PartialUpdateExtractedField>;

export type EntityResponseType = HttpResponse<IExtractedField>;
export type EntityArrayResponseType = HttpResponse<IExtractedField[]>;

@Injectable({ providedIn: 'root' })
export class ExtractedFieldService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extracted-fields', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/extracted-fields/_search', 'documentservice');

  create(extractedField: NewExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedField);
    return this.http
      .post<RestExtractedField>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(extractedField: IExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedField);
    return this.http
      .put<RestExtractedField>(`${this.resourceUrl}/${encodeURIComponent(this.getExtractedFieldIdentifier(extractedField))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(extractedField: PartialUpdateExtractedField): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extractedField);
    return this.http
      .patch<RestExtractedField>(`${this.resourceUrl}/${encodeURIComponent(this.getExtractedFieldIdentifier(extractedField))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExtractedField>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExtractedField[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExtractedField[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExtractedField[]>()], asapScheduler)),
    );
  }

  getExtractedFieldIdentifier(extractedField: Pick<IExtractedField, 'id'>): number {
    return extractedField.id;
  }

  compareExtractedField(o1: Pick<IExtractedField, 'id'> | null, o2: Pick<IExtractedField, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtractedFieldIdentifier(o1) === this.getExtractedFieldIdentifier(o2) : o1 === o2;
  }

  addExtractedFieldToCollectionIfMissing<Type extends Pick<IExtractedField, 'id'>>(
    extractedFieldCollection: Type[],
    ...extractedFieldsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extractedFields: Type[] = extractedFieldsToCheck.filter(isPresent);
    if (extractedFields.length > 0) {
      const extractedFieldCollectionIdentifiers = extractedFieldCollection.map(extractedFieldItem =>
        this.getExtractedFieldIdentifier(extractedFieldItem),
      );
      const extractedFieldsToAdd = extractedFields.filter(extractedFieldItem => {
        const extractedFieldIdentifier = this.getExtractedFieldIdentifier(extractedFieldItem);
        if (extractedFieldCollectionIdentifiers.includes(extractedFieldIdentifier)) {
          return false;
        }
        extractedFieldCollectionIdentifiers.push(extractedFieldIdentifier);
        return true;
      });
      return [...extractedFieldsToAdd, ...extractedFieldCollection];
    }
    return extractedFieldCollection;
  }

  protected convertDateFromClient<T extends IExtractedField | NewExtractedField | PartialUpdateExtractedField>(
    extractedField: T,
  ): RestOf<T> {
    return {
      ...extractedField,
      extractedDate: extractedField.extractedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExtractedField: RestExtractedField): IExtractedField {
    return {
      ...restExtractedField,
      extractedDate: restExtractedField.extractedDate ? dayjs(restExtractedField.extractedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExtractedField>): HttpResponse<IExtractedField> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExtractedField[]>): HttpResponse<IExtractedField[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
