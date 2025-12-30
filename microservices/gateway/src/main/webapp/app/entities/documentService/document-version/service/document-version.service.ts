import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentVersion, NewDocumentVersion } from '../document-version.model';

export type PartialUpdateDocumentVersion = Partial<IDocumentVersion> & Pick<IDocumentVersion, 'id'>;

type RestOf<T extends IDocumentVersion | NewDocumentVersion> = Omit<T, 'uploadDate'> & {
  uploadDate?: string | null;
};

export type RestDocumentVersion = RestOf<IDocumentVersion>;

export type NewRestDocumentVersion = RestOf<NewDocumentVersion>;

export type PartialUpdateRestDocumentVersion = RestOf<PartialUpdateDocumentVersion>;

export type EntityResponseType = HttpResponse<IDocumentVersion>;
export type EntityArrayResponseType = HttpResponse<IDocumentVersion[]>;

@Injectable({ providedIn: 'root' })
export class DocumentVersionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-versions', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-versions/_search', 'documentservice');

  create(documentVersion: NewDocumentVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentVersion);
    return this.http
      .post<RestDocumentVersion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentVersion: IDocumentVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentVersion);
    return this.http
      .put<RestDocumentVersion>(`${this.resourceUrl}/${this.getDocumentVersionIdentifier(documentVersion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentVersion: PartialUpdateDocumentVersion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentVersion);
    return this.http
      .patch<RestDocumentVersion>(`${this.resourceUrl}/${this.getDocumentVersionIdentifier(documentVersion)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentVersion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentVersion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentVersion[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentVersion[]>()], asapScheduler)),
    );
  }

  getDocumentVersionIdentifier(documentVersion: Pick<IDocumentVersion, 'id'>): number {
    return documentVersion.id;
  }

  compareDocumentVersion(o1: Pick<IDocumentVersion, 'id'> | null, o2: Pick<IDocumentVersion, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentVersionIdentifier(o1) === this.getDocumentVersionIdentifier(o2) : o1 === o2;
  }

  addDocumentVersionToCollectionIfMissing<Type extends Pick<IDocumentVersion, 'id'>>(
    documentVersionCollection: Type[],
    ...documentVersionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentVersions: Type[] = documentVersionsToCheck.filter(isPresent);
    if (documentVersions.length > 0) {
      const documentVersionCollectionIdentifiers = documentVersionCollection.map(documentVersionItem =>
        this.getDocumentVersionIdentifier(documentVersionItem),
      );
      const documentVersionsToAdd = documentVersions.filter(documentVersionItem => {
        const documentVersionIdentifier = this.getDocumentVersionIdentifier(documentVersionItem);
        if (documentVersionCollectionIdentifiers.includes(documentVersionIdentifier)) {
          return false;
        }
        documentVersionCollectionIdentifiers.push(documentVersionIdentifier);
        return true;
      });
      return [...documentVersionsToAdd, ...documentVersionCollection];
    }
    return documentVersionCollection;
  }

  protected convertDateFromClient<T extends IDocumentVersion | NewDocumentVersion | PartialUpdateDocumentVersion>(
    documentVersion: T,
  ): RestOf<T> {
    return {
      ...documentVersion,
      uploadDate: documentVersion.uploadDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentVersion: RestDocumentVersion): IDocumentVersion {
    return {
      ...restDocumentVersion,
      uploadDate: restDocumentVersion.uploadDate ? dayjs(restDocumentVersion.uploadDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentVersion>): HttpResponse<IDocumentVersion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentVersion[]>): HttpResponse<IDocumentVersion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
