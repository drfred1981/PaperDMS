import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IWorkflowInstance, NewWorkflowInstance } from '../workflow-instance.model';

export type PartialUpdateWorkflowInstance = Partial<IWorkflowInstance> & Pick<IWorkflowInstance, 'id'>;

type RestOf<T extends IWorkflowInstance | NewWorkflowInstance> = Omit<T, 'startDate' | 'dueDate' | 'completedDate' | 'cancelledDate'> & {
  startDate?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  cancelledDate?: string | null;
};

export type RestWorkflowInstance = RestOf<IWorkflowInstance>;

export type NewRestWorkflowInstance = RestOf<NewWorkflowInstance>;

export type PartialUpdateRestWorkflowInstance = RestOf<PartialUpdateWorkflowInstance>;

export type EntityResponseType = HttpResponse<IWorkflowInstance>;
export type EntityArrayResponseType = HttpResponse<IWorkflowInstance[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowInstanceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workflow-instances', 'workflowservice');

  create(workflowInstance: NewWorkflowInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowInstance);
    return this.http
      .post<RestWorkflowInstance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workflowInstance: IWorkflowInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowInstance);
    return this.http
      .put<RestWorkflowInstance>(`${this.resourceUrl}/${encodeURIComponent(this.getWorkflowInstanceIdentifier(workflowInstance))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workflowInstance: PartialUpdateWorkflowInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowInstance);
    return this.http
      .patch<RestWorkflowInstance>(
        `${this.resourceUrl}/${encodeURIComponent(this.getWorkflowInstanceIdentifier(workflowInstance))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkflowInstance>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkflowInstance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getWorkflowInstanceIdentifier(workflowInstance: Pick<IWorkflowInstance, 'id'>): number {
    return workflowInstance.id;
  }

  compareWorkflowInstance(o1: Pick<IWorkflowInstance, 'id'> | null, o2: Pick<IWorkflowInstance, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkflowInstanceIdentifier(o1) === this.getWorkflowInstanceIdentifier(o2) : o1 === o2;
  }

  addWorkflowInstanceToCollectionIfMissing<Type extends Pick<IWorkflowInstance, 'id'>>(
    workflowInstanceCollection: Type[],
    ...workflowInstancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workflowInstances: Type[] = workflowInstancesToCheck.filter(isPresent);
    if (workflowInstances.length > 0) {
      const workflowInstanceCollectionIdentifiers = workflowInstanceCollection.map(workflowInstanceItem =>
        this.getWorkflowInstanceIdentifier(workflowInstanceItem),
      );
      const workflowInstancesToAdd = workflowInstances.filter(workflowInstanceItem => {
        const workflowInstanceIdentifier = this.getWorkflowInstanceIdentifier(workflowInstanceItem);
        if (workflowInstanceCollectionIdentifiers.includes(workflowInstanceIdentifier)) {
          return false;
        }
        workflowInstanceCollectionIdentifiers.push(workflowInstanceIdentifier);
        return true;
      });
      return [...workflowInstancesToAdd, ...workflowInstanceCollection];
    }
    return workflowInstanceCollection;
  }

  protected convertDateFromClient<T extends IWorkflowInstance | NewWorkflowInstance | PartialUpdateWorkflowInstance>(
    workflowInstance: T,
  ): RestOf<T> {
    return {
      ...workflowInstance,
      startDate: workflowInstance.startDate?.toJSON() ?? null,
      dueDate: workflowInstance.dueDate?.toJSON() ?? null,
      completedDate: workflowInstance.completedDate?.toJSON() ?? null,
      cancelledDate: workflowInstance.cancelledDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkflowInstance: RestWorkflowInstance): IWorkflowInstance {
    return {
      ...restWorkflowInstance,
      startDate: restWorkflowInstance.startDate ? dayjs(restWorkflowInstance.startDate) : undefined,
      dueDate: restWorkflowInstance.dueDate ? dayjs(restWorkflowInstance.dueDate) : undefined,
      completedDate: restWorkflowInstance.completedDate ? dayjs(restWorkflowInstance.completedDate) : undefined,
      cancelledDate: restWorkflowInstance.cancelledDate ? dayjs(restWorkflowInstance.cancelledDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkflowInstance>): HttpResponse<IWorkflowInstance> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkflowInstance[]>): HttpResponse<IWorkflowInstance[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
