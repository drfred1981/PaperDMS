import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaSmartFolder, NewMetaSmartFolder } from '../meta-smart-folder.model';

export type PartialUpdateMetaSmartFolder = Partial<IMetaSmartFolder> & Pick<IMetaSmartFolder, 'id'>;

type RestOf<T extends IMetaSmartFolder | NewMetaSmartFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaSmartFolder = RestOf<IMetaSmartFolder>;

export type NewRestMetaSmartFolder = RestOf<NewMetaSmartFolder>;

export type PartialUpdateRestMetaSmartFolder = RestOf<PartialUpdateMetaSmartFolder>;

export type EntityResponseType = HttpResponse<IMetaSmartFolder>;
export type EntityArrayResponseType = HttpResponse<IMetaSmartFolder[]>;

@Injectable({ providedIn: 'root' })
export class MetaSmartFolderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-smart-folders', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-smart-folders/_search', 'documentservice');

  create(metaSmartFolder: NewMetaSmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSmartFolder);
    return this.http
      .post<RestMetaSmartFolder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaSmartFolder: IMetaSmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSmartFolder);
    return this.http
      .put<RestMetaSmartFolder>(`${this.resourceUrl}/${this.getMetaSmartFolderIdentifier(metaSmartFolder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaSmartFolder: PartialUpdateMetaSmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSmartFolder);
    return this.http
      .patch<RestMetaSmartFolder>(`${this.resourceUrl}/${this.getMetaSmartFolderIdentifier(metaSmartFolder)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaSmartFolder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaSmartFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaSmartFolder[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaSmartFolder[]>()], asapScheduler)),
    );
  }

  getMetaSmartFolderIdentifier(metaSmartFolder: Pick<IMetaSmartFolder, 'id'>): number {
    return metaSmartFolder.id;
  }

  compareMetaSmartFolder(o1: Pick<IMetaSmartFolder, 'id'> | null, o2: Pick<IMetaSmartFolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaSmartFolderIdentifier(o1) === this.getMetaSmartFolderIdentifier(o2) : o1 === o2;
  }

  addMetaSmartFolderToCollectionIfMissing<Type extends Pick<IMetaSmartFolder, 'id'>>(
    metaSmartFolderCollection: Type[],
    ...metaSmartFoldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaSmartFolders: Type[] = metaSmartFoldersToCheck.filter(isPresent);
    if (metaSmartFolders.length > 0) {
      const metaSmartFolderCollectionIdentifiers = metaSmartFolderCollection.map(metaSmartFolderItem =>
        this.getMetaSmartFolderIdentifier(metaSmartFolderItem),
      );
      const metaSmartFoldersToAdd = metaSmartFolders.filter(metaSmartFolderItem => {
        const metaSmartFolderIdentifier = this.getMetaSmartFolderIdentifier(metaSmartFolderItem);
        if (metaSmartFolderCollectionIdentifiers.includes(metaSmartFolderIdentifier)) {
          return false;
        }
        metaSmartFolderCollectionIdentifiers.push(metaSmartFolderIdentifier);
        return true;
      });
      return [...metaSmartFoldersToAdd, ...metaSmartFolderCollection];
    }
    return metaSmartFolderCollection;
  }

  protected convertDateFromClient<T extends IMetaSmartFolder | NewMetaSmartFolder | PartialUpdateMetaSmartFolder>(
    metaSmartFolder: T,
  ): RestOf<T> {
    return {
      ...metaSmartFolder,
      createdDate: metaSmartFolder.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaSmartFolder: RestMetaSmartFolder): IMetaSmartFolder {
    return {
      ...restMetaSmartFolder,
      createdDate: restMetaSmartFolder.createdDate ? dayjs(restMetaSmartFolder.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaSmartFolder>): HttpResponse<IMetaSmartFolder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaSmartFolder[]>): HttpResponse<IMetaSmartFolder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
