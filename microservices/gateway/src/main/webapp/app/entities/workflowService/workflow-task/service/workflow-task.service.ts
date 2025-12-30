import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkflowTask, NewWorkflowTask } from '../workflow-task.model';

export type PartialUpdateWorkflowTask = Partial<IWorkflowTask> & Pick<IWorkflowTask, 'id'>;

type RestOf<T extends IWorkflowTask | NewWorkflowTask> = Omit<T, 'assignedDate' | 'dueDate' | 'completedDate' | 'delegatedDate'> & {
  assignedDate?: string | null;
  dueDate?: string | null;
  completedDate?: string | null;
  delegatedDate?: string | null;
};

export type RestWorkflowTask = RestOf<IWorkflowTask>;

export type NewRestWorkflowTask = RestOf<NewWorkflowTask>;

export type PartialUpdateRestWorkflowTask = RestOf<PartialUpdateWorkflowTask>;

export type EntityResponseType = HttpResponse<IWorkflowTask>;
export type EntityArrayResponseType = HttpResponse<IWorkflowTask[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowTaskService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workflow-tasks', 'workflowservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/workflow-tasks/_search', 'workflowservice');

  create(workflowTask: NewWorkflowTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowTask);
    return this.http
      .post<RestWorkflowTask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workflowTask: IWorkflowTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowTask);
    return this.http
      .put<RestWorkflowTask>(`${this.resourceUrl}/${this.getWorkflowTaskIdentifier(workflowTask)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workflowTask: PartialUpdateWorkflowTask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowTask);
    return this.http
      .patch<RestWorkflowTask>(`${this.resourceUrl}/${this.getWorkflowTaskIdentifier(workflowTask)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkflowTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkflowTask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestWorkflowTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IWorkflowTask[]>()], asapScheduler)),
    );
  }

  getWorkflowTaskIdentifier(workflowTask: Pick<IWorkflowTask, 'id'>): number {
    return workflowTask.id;
  }

  compareWorkflowTask(o1: Pick<IWorkflowTask, 'id'> | null, o2: Pick<IWorkflowTask, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkflowTaskIdentifier(o1) === this.getWorkflowTaskIdentifier(o2) : o1 === o2;
  }

  addWorkflowTaskToCollectionIfMissing<Type extends Pick<IWorkflowTask, 'id'>>(
    workflowTaskCollection: Type[],
    ...workflowTasksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workflowTasks: Type[] = workflowTasksToCheck.filter(isPresent);
    if (workflowTasks.length > 0) {
      const workflowTaskCollectionIdentifiers = workflowTaskCollection.map(workflowTaskItem =>
        this.getWorkflowTaskIdentifier(workflowTaskItem),
      );
      const workflowTasksToAdd = workflowTasks.filter(workflowTaskItem => {
        const workflowTaskIdentifier = this.getWorkflowTaskIdentifier(workflowTaskItem);
        if (workflowTaskCollectionIdentifiers.includes(workflowTaskIdentifier)) {
          return false;
        }
        workflowTaskCollectionIdentifiers.push(workflowTaskIdentifier);
        return true;
      });
      return [...workflowTasksToAdd, ...workflowTaskCollection];
    }
    return workflowTaskCollection;
  }

  protected convertDateFromClient<T extends IWorkflowTask | NewWorkflowTask | PartialUpdateWorkflowTask>(workflowTask: T): RestOf<T> {
    return {
      ...workflowTask,
      assignedDate: workflowTask.assignedDate?.toJSON() ?? null,
      dueDate: workflowTask.dueDate?.toJSON() ?? null,
      completedDate: workflowTask.completedDate?.toJSON() ?? null,
      delegatedDate: workflowTask.delegatedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkflowTask: RestWorkflowTask): IWorkflowTask {
    return {
      ...restWorkflowTask,
      assignedDate: restWorkflowTask.assignedDate ? dayjs(restWorkflowTask.assignedDate) : undefined,
      dueDate: restWorkflowTask.dueDate ? dayjs(restWorkflowTask.dueDate) : undefined,
      completedDate: restWorkflowTask.completedDate ? dayjs(restWorkflowTask.completedDate) : undefined,
      delegatedDate: restWorkflowTask.delegatedDate ? dayjs(restWorkflowTask.delegatedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkflowTask>): HttpResponse<IWorkflowTask> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkflowTask[]>): HttpResponse<IWorkflowTask[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
