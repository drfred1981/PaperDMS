import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkflowApprovalHistory, NewWorkflowApprovalHistory } from '../workflow-approval-history.model';

export type PartialUpdateWorkflowApprovalHistory = Partial<IWorkflowApprovalHistory> & Pick<IWorkflowApprovalHistory, 'id'>;

type RestOf<T extends IWorkflowApprovalHistory | NewWorkflowApprovalHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestWorkflowApprovalHistory = RestOf<IWorkflowApprovalHistory>;

export type NewRestWorkflowApprovalHistory = RestOf<NewWorkflowApprovalHistory>;

export type PartialUpdateRestWorkflowApprovalHistory = RestOf<PartialUpdateWorkflowApprovalHistory>;

export type EntityResponseType = HttpResponse<IWorkflowApprovalHistory>;
export type EntityArrayResponseType = HttpResponse<IWorkflowApprovalHistory[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowApprovalHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workflow-approval-histories', 'workflowservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/workflow-approval-histories/_search', 'workflowservice');

  create(workflowApprovalHistory: NewWorkflowApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowApprovalHistory);
    return this.http
      .post<RestWorkflowApprovalHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workflowApprovalHistory: IWorkflowApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowApprovalHistory);
    return this.http
      .put<RestWorkflowApprovalHistory>(`${this.resourceUrl}/${this.getWorkflowApprovalHistoryIdentifier(workflowApprovalHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workflowApprovalHistory: PartialUpdateWorkflowApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workflowApprovalHistory);
    return this.http
      .patch<RestWorkflowApprovalHistory>(
        `${this.resourceUrl}/${this.getWorkflowApprovalHistoryIdentifier(workflowApprovalHistory)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkflowApprovalHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkflowApprovalHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestWorkflowApprovalHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IWorkflowApprovalHistory[]>()], asapScheduler)),
    );
  }

  getWorkflowApprovalHistoryIdentifier(workflowApprovalHistory: Pick<IWorkflowApprovalHistory, 'id'>): number {
    return workflowApprovalHistory.id;
  }

  compareWorkflowApprovalHistory(
    o1: Pick<IWorkflowApprovalHistory, 'id'> | null,
    o2: Pick<IWorkflowApprovalHistory, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getWorkflowApprovalHistoryIdentifier(o1) === this.getWorkflowApprovalHistoryIdentifier(o2) : o1 === o2;
  }

  addWorkflowApprovalHistoryToCollectionIfMissing<Type extends Pick<IWorkflowApprovalHistory, 'id'>>(
    workflowApprovalHistoryCollection: Type[],
    ...workflowApprovalHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workflowApprovalHistories: Type[] = workflowApprovalHistoriesToCheck.filter(isPresent);
    if (workflowApprovalHistories.length > 0) {
      const workflowApprovalHistoryCollectionIdentifiers = workflowApprovalHistoryCollection.map(workflowApprovalHistoryItem =>
        this.getWorkflowApprovalHistoryIdentifier(workflowApprovalHistoryItem),
      );
      const workflowApprovalHistoriesToAdd = workflowApprovalHistories.filter(workflowApprovalHistoryItem => {
        const workflowApprovalHistoryIdentifier = this.getWorkflowApprovalHistoryIdentifier(workflowApprovalHistoryItem);
        if (workflowApprovalHistoryCollectionIdentifiers.includes(workflowApprovalHistoryIdentifier)) {
          return false;
        }
        workflowApprovalHistoryCollectionIdentifiers.push(workflowApprovalHistoryIdentifier);
        return true;
      });
      return [...workflowApprovalHistoriesToAdd, ...workflowApprovalHistoryCollection];
    }
    return workflowApprovalHistoryCollection;
  }

  protected convertDateFromClient<T extends IWorkflowApprovalHistory | NewWorkflowApprovalHistory | PartialUpdateWorkflowApprovalHistory>(
    workflowApprovalHistory: T,
  ): RestOf<T> {
    return {
      ...workflowApprovalHistory,
      actionDate: workflowApprovalHistory.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkflowApprovalHistory: RestWorkflowApprovalHistory): IWorkflowApprovalHistory {
    return {
      ...restWorkflowApprovalHistory,
      actionDate: restWorkflowApprovalHistory.actionDate ? dayjs(restWorkflowApprovalHistory.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkflowApprovalHistory>): HttpResponse<IWorkflowApprovalHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkflowApprovalHistory[]>): HttpResponse<IWorkflowApprovalHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
