import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaPermissionGroup, NewMetaPermissionGroup } from '../meta-permission-group.model';

export type PartialUpdateMetaPermissionGroup = Partial<IMetaPermissionGroup> & Pick<IMetaPermissionGroup, 'id'>;

type RestOf<T extends IMetaPermissionGroup | NewMetaPermissionGroup> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaPermissionGroup = RestOf<IMetaPermissionGroup>;

export type NewRestMetaPermissionGroup = RestOf<NewMetaPermissionGroup>;

export type PartialUpdateRestMetaPermissionGroup = RestOf<PartialUpdateMetaPermissionGroup>;

export type EntityResponseType = HttpResponse<IMetaPermissionGroup>;
export type EntityArrayResponseType = HttpResponse<IMetaPermissionGroup[]>;

@Injectable({ providedIn: 'root' })
export class MetaPermissionGroupService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-permission-groups', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-permission-groups/_search', 'documentservice');

  create(metaPermissionGroup: NewMetaPermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaPermissionGroup);
    return this.http
      .post<RestMetaPermissionGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaPermissionGroup: IMetaPermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaPermissionGroup);
    return this.http
      .put<RestMetaPermissionGroup>(`${this.resourceUrl}/${this.getMetaPermissionGroupIdentifier(metaPermissionGroup)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaPermissionGroup: PartialUpdateMetaPermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaPermissionGroup);
    return this.http
      .patch<RestMetaPermissionGroup>(`${this.resourceUrl}/${this.getMetaPermissionGroupIdentifier(metaPermissionGroup)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaPermissionGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaPermissionGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaPermissionGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaPermissionGroup[]>()], asapScheduler)),
    );
  }

  getMetaPermissionGroupIdentifier(metaPermissionGroup: Pick<IMetaPermissionGroup, 'id'>): number {
    return metaPermissionGroup.id;
  }

  compareMetaPermissionGroup(o1: Pick<IMetaPermissionGroup, 'id'> | null, o2: Pick<IMetaPermissionGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaPermissionGroupIdentifier(o1) === this.getMetaPermissionGroupIdentifier(o2) : o1 === o2;
  }

  addMetaPermissionGroupToCollectionIfMissing<Type extends Pick<IMetaPermissionGroup, 'id'>>(
    metaPermissionGroupCollection: Type[],
    ...metaPermissionGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaPermissionGroups: Type[] = metaPermissionGroupsToCheck.filter(isPresent);
    if (metaPermissionGroups.length > 0) {
      const metaPermissionGroupCollectionIdentifiers = metaPermissionGroupCollection.map(metaPermissionGroupItem =>
        this.getMetaPermissionGroupIdentifier(metaPermissionGroupItem),
      );
      const metaPermissionGroupsToAdd = metaPermissionGroups.filter(metaPermissionGroupItem => {
        const metaPermissionGroupIdentifier = this.getMetaPermissionGroupIdentifier(metaPermissionGroupItem);
        if (metaPermissionGroupCollectionIdentifiers.includes(metaPermissionGroupIdentifier)) {
          return false;
        }
        metaPermissionGroupCollectionIdentifiers.push(metaPermissionGroupIdentifier);
        return true;
      });
      return [...metaPermissionGroupsToAdd, ...metaPermissionGroupCollection];
    }
    return metaPermissionGroupCollection;
  }

  protected convertDateFromClient<T extends IMetaPermissionGroup | NewMetaPermissionGroup | PartialUpdateMetaPermissionGroup>(
    metaPermissionGroup: T,
  ): RestOf<T> {
    return {
      ...metaPermissionGroup,
      createdDate: metaPermissionGroup.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaPermissionGroup: RestMetaPermissionGroup): IMetaPermissionGroup {
    return {
      ...restMetaPermissionGroup,
      createdDate: restMetaPermissionGroup.createdDate ? dayjs(restMetaPermissionGroup.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaPermissionGroup>): HttpResponse<IMetaPermissionGroup> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaPermissionGroup[]>): HttpResponse<IMetaPermissionGroup[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
