import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { IWorkflow, NewWorkflow } from '../workflow.model';

export type PartialUpdateWorkflow = Partial<IWorkflow> & Pick<IWorkflow, 'id'>;

type RestOf<T extends IWorkflow | NewWorkflow> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestWorkflow = RestOf<IWorkflow>;

export type NewRestWorkflow = RestOf<NewWorkflow>;

export type PartialUpdateRestWorkflow = RestOf<PartialUpdateWorkflow>;

export type EntityResponseType = HttpResponse<IWorkflow>;
export type EntityArrayResponseType = HttpResponse<IWorkflow[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workflows', 'workflowservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/workflows/_search', 'workflowservice');

  create(workflow: NewWorkflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflow);
    return this.http
      .post<RestWorkflow>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workflow: IWorkflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflow);
    return this.http
      .put<RestWorkflow>(`${this.resourceUrl}/${encodeURIComponent(this.getWorkflowIdentifier(workflow))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workflow: PartialUpdateWorkflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflow);
    return this.http
      .patch<RestWorkflow>(`${this.resourceUrl}/${encodeURIComponent(this.getWorkflowIdentifier(workflow))}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkflow>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkflow[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestWorkflow[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IWorkflow[]>()], asapScheduler)),
    );
  }

  getWorkflowIdentifier(workflow: Pick<IWorkflow, 'id'>): number {
    return workflow.id;
  }

  compareWorkflow(o1: Pick<IWorkflow, 'id'> | null, o2: Pick<IWorkflow, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkflowIdentifier(o1) === this.getWorkflowIdentifier(o2) : o1 === o2;
  }

  addWorkflowToCollectionIfMissing<Type extends Pick<IWorkflow, 'id'>>(
    workflowCollection: Type[],
    ...workflowsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workflows: Type[] = workflowsToCheck.filter(isPresent);
    if (workflows.length > 0) {
      const workflowCollectionIdentifiers = workflowCollection.map(workflowItem => this.getWorkflowIdentifier(workflowItem));
      const workflowsToAdd = workflows.filter(workflowItem => {
        const workflowIdentifier = this.getWorkflowIdentifier(workflowItem);
        if (workflowCollectionIdentifiers.includes(workflowIdentifier)) {
          return false;
        }
        workflowCollectionIdentifiers.push(workflowIdentifier);
        return true;
      });
      return [...workflowsToAdd, ...workflowCollection];
    }
    return workflowCollection;
  }

  protected convertDateFromClient<T extends IWorkflow | NewWorkflow | PartialUpdateWorkflow>(workflow: T): RestOf<T> {
    return {
      ...workflow,
      createdDate: workflow.createdDate?.toJSON() ?? null,
      lastModifiedDate: workflow.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkflow: RestWorkflow): IWorkflow {
    return {
      ...restWorkflow,
      createdDate: restWorkflow.createdDate ? dayjs(restWorkflow.createdDate) : undefined,
      lastModifiedDate: restWorkflow.lastModifiedDate ? dayjs(restWorkflow.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkflow>): HttpResponse<IWorkflow> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkflow[]>): HttpResponse<IWorkflow[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
