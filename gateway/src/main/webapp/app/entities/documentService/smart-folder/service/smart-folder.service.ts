import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISmartFolder, NewSmartFolder } from '../smart-folder.model';

export type PartialUpdateSmartFolder = Partial<ISmartFolder> & Pick<ISmartFolder, 'id'>;

type RestOf<T extends ISmartFolder | NewSmartFolder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestSmartFolder = RestOf<ISmartFolder>;

export type NewRestSmartFolder = RestOf<NewSmartFolder>;

export type PartialUpdateRestSmartFolder = RestOf<PartialUpdateSmartFolder>;

export type EntityResponseType = HttpResponse<ISmartFolder>;
export type EntityArrayResponseType = HttpResponse<ISmartFolder[]>;

@Injectable({ providedIn: 'root' })
export class SmartFolderService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/smart-folders', 'documentservice');

  create(smartFolder: NewSmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smartFolder);
    return this.http
      .post<RestSmartFolder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(smartFolder: ISmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smartFolder);
    return this.http
      .put<RestSmartFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getSmartFolderIdentifier(smartFolder))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(smartFolder: PartialUpdateSmartFolder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smartFolder);
    return this.http
      .patch<RestSmartFolder>(`${this.resourceUrl}/${encodeURIComponent(this.getSmartFolderIdentifier(smartFolder))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSmartFolder>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSmartFolder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSmartFolderIdentifier(smartFolder: Pick<ISmartFolder, 'id'>): number {
    return smartFolder.id;
  }

  compareSmartFolder(o1: Pick<ISmartFolder, 'id'> | null, o2: Pick<ISmartFolder, 'id'> | null): boolean {
    return o1 && o2 ? this.getSmartFolderIdentifier(o1) === this.getSmartFolderIdentifier(o2) : o1 === o2;
  }

  addSmartFolderToCollectionIfMissing<Type extends Pick<ISmartFolder, 'id'>>(
    smartFolderCollection: Type[],
    ...smartFoldersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const smartFolders: Type[] = smartFoldersToCheck.filter(isPresent);
    if (smartFolders.length > 0) {
      const smartFolderCollectionIdentifiers = smartFolderCollection.map(smartFolderItem => this.getSmartFolderIdentifier(smartFolderItem));
      const smartFoldersToAdd = smartFolders.filter(smartFolderItem => {
        const smartFolderIdentifier = this.getSmartFolderIdentifier(smartFolderItem);
        if (smartFolderCollectionIdentifiers.includes(smartFolderIdentifier)) {
          return false;
        }
        smartFolderCollectionIdentifiers.push(smartFolderIdentifier);
        return true;
      });
      return [...smartFoldersToAdd, ...smartFolderCollection];
    }
    return smartFolderCollection;
  }

  protected convertDateFromClient<T extends ISmartFolder | NewSmartFolder | PartialUpdateSmartFolder>(smartFolder: T): RestOf<T> {
    return {
      ...smartFolder,
      createdDate: smartFolder.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSmartFolder: RestSmartFolder): ISmartFolder {
    return {
      ...restSmartFolder,
      createdDate: restSmartFolder.createdDate ? dayjs(restSmartFolder.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSmartFolder>): HttpResponse<ISmartFolder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSmartFolder[]>): HttpResponse<ISmartFolder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
