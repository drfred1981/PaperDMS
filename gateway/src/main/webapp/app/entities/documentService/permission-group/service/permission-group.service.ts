import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermissionGroup, NewPermissionGroup } from '../permission-group.model';

export type PartialUpdatePermissionGroup = Partial<IPermissionGroup> & Pick<IPermissionGroup, 'id'>;

type RestOf<T extends IPermissionGroup | NewPermissionGroup> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestPermissionGroup = RestOf<IPermissionGroup>;

export type NewRestPermissionGroup = RestOf<NewPermissionGroup>;

export type PartialUpdateRestPermissionGroup = RestOf<PartialUpdatePermissionGroup>;

export type EntityResponseType = HttpResponse<IPermissionGroup>;
export type EntityArrayResponseType = HttpResponse<IPermissionGroup[]>;

@Injectable({ providedIn: 'root' })
export class PermissionGroupService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permission-groups', 'documentservice');

  create(permissionGroup: NewPermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permissionGroup);
    return this.http
      .post<RestPermissionGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(permissionGroup: IPermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permissionGroup);
    return this.http
      .put<RestPermissionGroup>(`${this.resourceUrl}/${this.getPermissionGroupIdentifier(permissionGroup)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(permissionGroup: PartialUpdatePermissionGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(permissionGroup);
    return this.http
      .patch<RestPermissionGroup>(`${this.resourceUrl}/${this.getPermissionGroupIdentifier(permissionGroup)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPermissionGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPermissionGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPermissionGroupIdentifier(permissionGroup: Pick<IPermissionGroup, 'id'>): number {
    return permissionGroup.id;
  }

  comparePermissionGroup(o1: Pick<IPermissionGroup, 'id'> | null, o2: Pick<IPermissionGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getPermissionGroupIdentifier(o1) === this.getPermissionGroupIdentifier(o2) : o1 === o2;
  }

  addPermissionGroupToCollectionIfMissing<Type extends Pick<IPermissionGroup, 'id'>>(
    permissionGroupCollection: Type[],
    ...permissionGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const permissionGroups: Type[] = permissionGroupsToCheck.filter(isPresent);
    if (permissionGroups.length > 0) {
      const permissionGroupCollectionIdentifiers = permissionGroupCollection.map(permissionGroupItem =>
        this.getPermissionGroupIdentifier(permissionGroupItem),
      );
      const permissionGroupsToAdd = permissionGroups.filter(permissionGroupItem => {
        const permissionGroupIdentifier = this.getPermissionGroupIdentifier(permissionGroupItem);
        if (permissionGroupCollectionIdentifiers.includes(permissionGroupIdentifier)) {
          return false;
        }
        permissionGroupCollectionIdentifiers.push(permissionGroupIdentifier);
        return true;
      });
      return [...permissionGroupsToAdd, ...permissionGroupCollection];
    }
    return permissionGroupCollection;
  }

  protected convertDateFromClient<T extends IPermissionGroup | NewPermissionGroup | PartialUpdatePermissionGroup>(
    permissionGroup: T,
  ): RestOf<T> {
    return {
      ...permissionGroup,
      createdDate: permissionGroup.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPermissionGroup: RestPermissionGroup): IPermissionGroup {
    return {
      ...restPermissionGroup,
      createdDate: restPermissionGroup.createdDate ? dayjs(restPermissionGroup.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPermissionGroup>): HttpResponse<IPermissionGroup> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPermissionGroup[]>): HttpResponse<IPermissionGroup[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
