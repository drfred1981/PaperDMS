import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentPermission, NewDocumentPermission } from '../document-permission.model';

export type PartialUpdateDocumentPermission = Partial<IDocumentPermission> & Pick<IDocumentPermission, 'id'>;

type RestOf<T extends IDocumentPermission | NewDocumentPermission> = Omit<T, 'grantedDate'> & {
  grantedDate?: string | null;
};

export type RestDocumentPermission = RestOf<IDocumentPermission>;

export type NewRestDocumentPermission = RestOf<NewDocumentPermission>;

export type PartialUpdateRestDocumentPermission = RestOf<PartialUpdateDocumentPermission>;

export type EntityResponseType = HttpResponse<IDocumentPermission>;
export type EntityArrayResponseType = HttpResponse<IDocumentPermission[]>;

@Injectable({ providedIn: 'root' })
export class DocumentPermissionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-permissions', 'documentservice');

  create(documentPermission: NewDocumentPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentPermission);
    return this.http
      .post<RestDocumentPermission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentPermission: IDocumentPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentPermission);
    return this.http
      .put<RestDocumentPermission>(`${this.resourceUrl}/${this.getDocumentPermissionIdentifier(documentPermission)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentPermission: PartialUpdateDocumentPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentPermission);
    return this.http
      .patch<RestDocumentPermission>(`${this.resourceUrl}/${this.getDocumentPermissionIdentifier(documentPermission)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentPermission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDocumentPermissionIdentifier(documentPermission: Pick<IDocumentPermission, 'id'>): number {
    return documentPermission.id;
  }

  compareDocumentPermission(o1: Pick<IDocumentPermission, 'id'> | null, o2: Pick<IDocumentPermission, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentPermissionIdentifier(o1) === this.getDocumentPermissionIdentifier(o2) : o1 === o2;
  }

  addDocumentPermissionToCollectionIfMissing<Type extends Pick<IDocumentPermission, 'id'>>(
    documentPermissionCollection: Type[],
    ...documentPermissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentPermissions: Type[] = documentPermissionsToCheck.filter(isPresent);
    if (documentPermissions.length > 0) {
      const documentPermissionCollectionIdentifiers = documentPermissionCollection.map(documentPermissionItem =>
        this.getDocumentPermissionIdentifier(documentPermissionItem),
      );
      const documentPermissionsToAdd = documentPermissions.filter(documentPermissionItem => {
        const documentPermissionIdentifier = this.getDocumentPermissionIdentifier(documentPermissionItem);
        if (documentPermissionCollectionIdentifiers.includes(documentPermissionIdentifier)) {
          return false;
        }
        documentPermissionCollectionIdentifiers.push(documentPermissionIdentifier);
        return true;
      });
      return [...documentPermissionsToAdd, ...documentPermissionCollection];
    }
    return documentPermissionCollection;
  }

  protected convertDateFromClient<T extends IDocumentPermission | NewDocumentPermission | PartialUpdateDocumentPermission>(
    documentPermission: T,
  ): RestOf<T> {
    return {
      ...documentPermission,
      grantedDate: documentPermission.grantedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentPermission: RestDocumentPermission): IDocumentPermission {
    return {
      ...restDocumentPermission,
      grantedDate: restDocumentPermission.grantedDate ? dayjs(restDocumentPermission.grantedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentPermission>): HttpResponse<IDocumentPermission> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentPermission[]>): HttpResponse<IDocumentPermission[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
