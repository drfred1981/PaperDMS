import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaFolder, NewMetaFolder } from '../meta-folder.model';

export type PartialUpdateMetaFolder = Partial<IMetaFolder> & Pick<IMetaFolder, 'id'>;

type RestOf<T extends IMetaFolder | NewMetaFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaFolder = RestOf<IMetaFolder>;

export type NewRestMetaFolder = RestOf<NewMetaFolder>;

export type PartialUpdateRestMetaFolder = RestOf<PartialUpdateMetaFolder>;

export type EntityResponseType = HttpResponse<IMetaFolder>;
export type EntityArrayResponseType = HttpResponse<IMetaFolder[]>;

@Injectable({ providedIn: 'root' })
export class MetaFolderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-folders', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-folders/_search', 'documentservice');

  create(metaFolder: NewMetaFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaFolder);
    return this.http
      .post<RestMetaFolder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaFolder: IMetaFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaFolder);
    return this.http
      .put<RestMetaFolder>(`${this.resourceUrl}/${this.getMetaFolderIdentifier(metaFolder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaFolder: PartialUpdateMetaFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaFolder);
    return this.http
      .patch<RestMetaFolder>(`${this.resourceUrl}/${this.getMetaFolderIdentifier(metaFolder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaFolder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaFolder[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaFolder[]>()], asapScheduler)),
    );
  }

  getMetaFolderIdentifier(metaFolder: Pick<IMetaFolder, 'id'>): number {
    return metaFolder.id;
  }

  compareMetaFolder(o1: Pick<IMetaFolder, 'id'> | null, o2: Pick<IMetaFolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaFolderIdentifier(o1) === this.getMetaFolderIdentifier(o2) : o1 === o2;
  }

  addMetaFolderToCollectionIfMissing<Type extends Pick<IMetaFolder, 'id'>>(
    metaFolderCollection: Type[],
    ...metaFoldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaFolders: Type[] = metaFoldersToCheck.filter(isPresent);
    if (metaFolders.length > 0) {
      const metaFolderCollectionIdentifiers = metaFolderCollection.map(metaFolderItem => this.getMetaFolderIdentifier(metaFolderItem));
      const metaFoldersToAdd = metaFolders.filter(metaFolderItem => {
        const metaFolderIdentifier = this.getMetaFolderIdentifier(metaFolderItem);
        if (metaFolderCollectionIdentifiers.includes(metaFolderIdentifier)) {
          return false;
        }
        metaFolderCollectionIdentifiers.push(metaFolderIdentifier);
        return true;
      });
      return [...metaFoldersToAdd, ...metaFolderCollection];
    }
    return metaFolderCollection;
  }

  protected convertDateFromClient<T extends IMetaFolder | NewMetaFolder | PartialUpdateMetaFolder>(metaFolder: T): RestOf<T> {
    return {
      ...metaFolder,
      createdDate: metaFolder.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaFolder: RestMetaFolder): IMetaFolder {
    return {
      ...restMetaFolder,
      createdDate: restMetaFolder.createdDate ? dayjs(restMetaFolder.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaFolder>): HttpResponse<IMetaFolder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaFolder[]>): HttpResponse<IMetaFolder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
