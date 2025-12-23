import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IFolder, NewFolder } from '../folder.model';

export type PartialUpdateFolder = Partial<IFolder> & Pick<IFolder, 'id'>;

type RestOf<T extends IFolder | NewFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestFolder = RestOf<IFolder>;

export type NewRestFolder = RestOf<NewFolder>;

export type PartialUpdateRestFolder = RestOf<PartialUpdateFolder>;

export type EntityResponseType = HttpResponse<IFolder>;
export type EntityArrayResponseType = HttpResponse<IFolder[]>;

@Injectable({ providedIn: 'root' })
export class FolderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/folders', 'documentservice');

  create(folder: NewFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(folder);
    return this.http
      .post<RestFolder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(folder: IFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(folder);
    return this.http
      .put<RestFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getFolderIdentifier(folder))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(folder: PartialUpdateFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(folder);
    return this.http
      .patch<RestFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getFolderIdentifier(folder))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFolder>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getFolderIdentifier(folder: Pick<IFolder, 'id'>): number {
    return folder.id;
  }

  compareFolder(o1: Pick<IFolder, 'id'> | null, o2: Pick<IFolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getFolderIdentifier(o1) === this.getFolderIdentifier(o2) : o1 === o2;
  }

  addFolderToCollectionIfMissing<Type extends Pick<IFolder, 'id'>>(
    folderCollection: Type[],
    ...foldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const folders: Type[] = foldersToCheck.filter(isPresent);
    if (folders.length > 0) {
      const folderCollectionIdentifiers = folderCollection.map(folderItem => this.getFolderIdentifier(folderItem));
      const foldersToAdd = folders.filter(folderItem => {
        const folderIdentifier = this.getFolderIdentifier(folderItem);
        if (folderCollectionIdentifiers.includes(folderIdentifier)) {
          return false;
        }
        folderCollectionIdentifiers.push(folderIdentifier);
        return true;
      });
      return [...foldersToAdd, ...folderCollection];
    }
    return folderCollection;
  }

  protected convertDateFromClient<T extends IFolder | NewFolder | PartialUpdateFolder>(folder: T): RestOf<T> {
    return {
      ...folder,
      createdDate: folder.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFolder: RestFolder): IFolder {
    return {
      ...restFolder,
      createdDate: restFolder.createdDate ? dayjs(restFolder.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFolder>): HttpResponse<IFolder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFolder[]>): HttpResponse<IFolder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
