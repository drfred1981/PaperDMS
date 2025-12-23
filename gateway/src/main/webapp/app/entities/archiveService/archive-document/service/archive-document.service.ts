import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IArchiveDocument, NewArchiveDocument } from '../archive-document.model';

export type PartialUpdateArchiveDocument = Partial<IArchiveDocument> & Pick<IArchiveDocument, 'id'>;

type RestOf<T extends IArchiveDocument | NewArchiveDocument> = Omit<T, 'addedDate'> & {
  addedDate?: string | null;
};

export type RestArchiveDocument = RestOf<IArchiveDocument>;

export type NewRestArchiveDocument = RestOf<NewArchiveDocument>;

export type PartialUpdateRestArchiveDocument = RestOf<PartialUpdateArchiveDocument>;

export type EntityResponseType = HttpResponse<IArchiveDocument>;
export type EntityArrayResponseType = HttpResponse<IArchiveDocument[]>;

@Injectable({ providedIn: 'root' })
export class ArchiveDocumentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/archive-documents', 'archiveservice');

  create(archiveDocument: NewArchiveDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveDocument);
    return this.http
      .post<RestArchiveDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(archiveDocument: IArchiveDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveDocument);
    return this.http
      .put<RestArchiveDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getArchiveDocumentIdentifier(archiveDocument))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(archiveDocument: PartialUpdateArchiveDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(archiveDocument);
    return this.http
      .patch<RestArchiveDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getArchiveDocumentIdentifier(archiveDocument))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestArchiveDocument>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestArchiveDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getArchiveDocumentIdentifier(archiveDocument: Pick<IArchiveDocument, 'id'>): number {
    return archiveDocument.id;
  }

  compareArchiveDocument(o1: Pick<IArchiveDocument, 'id'> | null, o2: Pick<IArchiveDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getArchiveDocumentIdentifier(o1) === this.getArchiveDocumentIdentifier(o2) : o1 === o2;
  }

  addArchiveDocumentToCollectionIfMissing<Type extends Pick<IArchiveDocument, 'id'>>(
    archiveDocumentCollection: Type[],
    ...archiveDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const archiveDocuments: Type[] = archiveDocumentsToCheck.filter(isPresent);
    if (archiveDocuments.length > 0) {
      const archiveDocumentCollectionIdentifiers = archiveDocumentCollection.map(archiveDocumentItem =>
        this.getArchiveDocumentIdentifier(archiveDocumentItem),
      );
      const archiveDocumentsToAdd = archiveDocuments.filter(archiveDocumentItem => {
        const archiveDocumentIdentifier = this.getArchiveDocumentIdentifier(archiveDocumentItem);
        if (archiveDocumentCollectionIdentifiers.includes(archiveDocumentIdentifier)) {
          return false;
        }
        archiveDocumentCollectionIdentifiers.push(archiveDocumentIdentifier);
        return true;
      });
      return [...archiveDocumentsToAdd, ...archiveDocumentCollection];
    }
    return archiveDocumentCollection;
  }

  protected convertDateFromClient<T extends IArchiveDocument | NewArchiveDocument | PartialUpdateArchiveDocument>(
    archiveDocument: T,
  ): RestOf<T> {
    return {
      ...archiveDocument,
      addedDate: archiveDocument.addedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restArchiveDocument: RestArchiveDocument): IArchiveDocument {
    return {
      ...restArchiveDocument,
      addedDate: restArchiveDocument.addedDate ? dayjs(restArchiveDocument.addedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestArchiveDocument>): HttpResponse<IArchiveDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestArchiveDocument[]>): HttpResponse<IArchiveDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
