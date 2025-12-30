import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmailImportDocument, NewEmailImportDocument } from '../email-import-document.model';

export type PartialUpdateEmailImportDocument = Partial<IEmailImportDocument> & Pick<IEmailImportDocument, 'id'>;

type RestOf<T extends IEmailImportDocument | NewEmailImportDocument> = Omit<T, 'receivedDate' | 'processedDate'> & {
  receivedDate?: string | null;
  processedDate?: string | null;
};

export type RestEmailImportDocument = RestOf<IEmailImportDocument>;

export type NewRestEmailImportDocument = RestOf<NewEmailImportDocument>;

export type PartialUpdateRestEmailImportDocument = RestOf<PartialUpdateEmailImportDocument>;

export type EntityResponseType = HttpResponse<IEmailImportDocument>;
export type EntityArrayResponseType = HttpResponse<IEmailImportDocument[]>;

@Injectable({ providedIn: 'root' })
export class EmailImportDocumentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-import-documents', 'emailimportdocumentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/email-import-documents/_search',
    'emailimportdocumentservice',
  );

  create(emailImportDocument: NewEmailImportDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportDocument);
    return this.http
      .post<RestEmailImportDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(emailImportDocument: IEmailImportDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportDocument);
    return this.http
      .put<RestEmailImportDocument>(`${this.resourceUrl}/${this.getEmailImportDocumentIdentifier(emailImportDocument)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(emailImportDocument: PartialUpdateEmailImportDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportDocument);
    return this.http
      .patch<RestEmailImportDocument>(`${this.resourceUrl}/${this.getEmailImportDocumentIdentifier(emailImportDocument)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmailImportDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmailImportDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEmailImportDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IEmailImportDocument[]>()], asapScheduler)),
    );
  }

  getEmailImportDocumentIdentifier(emailImportDocument: Pick<IEmailImportDocument, 'id'>): number {
    return emailImportDocument.id;
  }

  compareEmailImportDocument(o1: Pick<IEmailImportDocument, 'id'> | null, o2: Pick<IEmailImportDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmailImportDocumentIdentifier(o1) === this.getEmailImportDocumentIdentifier(o2) : o1 === o2;
  }

  addEmailImportDocumentToCollectionIfMissing<Type extends Pick<IEmailImportDocument, 'id'>>(
    emailImportDocumentCollection: Type[],
    ...emailImportDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emailImportDocuments: Type[] = emailImportDocumentsToCheck.filter(isPresent);
    if (emailImportDocuments.length > 0) {
      const emailImportDocumentCollectionIdentifiers = emailImportDocumentCollection.map(emailImportDocumentItem =>
        this.getEmailImportDocumentIdentifier(emailImportDocumentItem),
      );
      const emailImportDocumentsToAdd = emailImportDocuments.filter(emailImportDocumentItem => {
        const emailImportDocumentIdentifier = this.getEmailImportDocumentIdentifier(emailImportDocumentItem);
        if (emailImportDocumentCollectionIdentifiers.includes(emailImportDocumentIdentifier)) {
          return false;
        }
        emailImportDocumentCollectionIdentifiers.push(emailImportDocumentIdentifier);
        return true;
      });
      return [...emailImportDocumentsToAdd, ...emailImportDocumentCollection];
    }
    return emailImportDocumentCollection;
  }

  protected convertDateFromClient<T extends IEmailImportDocument | NewEmailImportDocument | PartialUpdateEmailImportDocument>(
    emailImportDocument: T,
  ): RestOf<T> {
    return {
      ...emailImportDocument,
      receivedDate: emailImportDocument.receivedDate?.toJSON() ?? null,
      processedDate: emailImportDocument.processedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmailImportDocument: RestEmailImportDocument): IEmailImportDocument {
    return {
      ...restEmailImportDocument,
      receivedDate: restEmailImportDocument.receivedDate ? dayjs(restEmailImportDocument.receivedDate) : undefined,
      processedDate: restEmailImportDocument.processedDate ? dayjs(restEmailImportDocument.processedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmailImportDocument>): HttpResponse<IEmailImportDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmailImportDocument[]>): HttpResponse<IEmailImportDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
