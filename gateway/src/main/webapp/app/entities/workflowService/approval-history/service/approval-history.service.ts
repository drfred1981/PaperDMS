import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IApprovalHistory, NewApprovalHistory } from '../approval-history.model';

export type PartialUpdateApprovalHistory = Partial<IApprovalHistory> & Pick<IApprovalHistory, 'id'>;

type RestOf<T extends IApprovalHistory | NewApprovalHistory> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestApprovalHistory = RestOf<IApprovalHistory>;

export type NewRestApprovalHistory = RestOf<NewApprovalHistory>;

export type PartialUpdateRestApprovalHistory = RestOf<PartialUpdateApprovalHistory>;

export type EntityResponseType = HttpResponse<IApprovalHistory>;
export type EntityArrayResponseType = HttpResponse<IApprovalHistory[]>;

@Injectable({ providedIn: 'root' })
export class ApprovalHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/approval-histories', 'workflowservice');

  create(approvalHistory: NewApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalHistory);
    return this.http
      .post<RestApprovalHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(approvalHistory: IApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalHistory);
    return this.http
      .put<RestApprovalHistory>(`${this.resourceUrl}/${encodeURIComponent(this.getApprovalHistoryIdentifier(approvalHistory))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(approvalHistory: PartialUpdateApprovalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(approvalHistory);
    return this.http
      .patch<RestApprovalHistory>(`${this.resourceUrl}/${encodeURIComponent(this.getApprovalHistoryIdentifier(approvalHistory))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestApprovalHistory>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestApprovalHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getApprovalHistoryIdentifier(approvalHistory: Pick<IApprovalHistory, 'id'>): number {
    return approvalHistory.id;
  }

  compareApprovalHistory(o1: Pick<IApprovalHistory, 'id'> | null, o2: Pick<IApprovalHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getApprovalHistoryIdentifier(o1) === this.getApprovalHistoryIdentifier(o2) : o1 === o2;
  }

  addApprovalHistoryToCollectionIfMissing<Type extends Pick<IApprovalHistory, 'id'>>(
    approvalHistoryCollection: Type[],
    ...approvalHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const approvalHistories: Type[] = approvalHistoriesToCheck.filter(isPresent);
    if (approvalHistories.length > 0) {
      const approvalHistoryCollectionIdentifiers = approvalHistoryCollection.map(approvalHistoryItem =>
        this.getApprovalHistoryIdentifier(approvalHistoryItem),
      );
      const approvalHistoriesToAdd = approvalHistories.filter(approvalHistoryItem => {
        const approvalHistoryIdentifier = this.getApprovalHistoryIdentifier(approvalHistoryItem);
        if (approvalHistoryCollectionIdentifiers.includes(approvalHistoryIdentifier)) {
          return false;
        }
        approvalHistoryCollectionIdentifiers.push(approvalHistoryIdentifier);
        return true;
      });
      return [...approvalHistoriesToAdd, ...approvalHistoryCollection];
    }
    return approvalHistoryCollection;
  }

  protected convertDateFromClient<T extends IApprovalHistory | NewApprovalHistory | PartialUpdateApprovalHistory>(
    approvalHistory: T,
  ): RestOf<T> {
    return {
      ...approvalHistory,
      actionDate: approvalHistory.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restApprovalHistory: RestApprovalHistory): IApprovalHistory {
    return {
      ...restApprovalHistory,
      actionDate: restApprovalHistory.actionDate ? dayjs(restApprovalHistory.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestApprovalHistory>): HttpResponse<IApprovalHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestApprovalHistory[]>): HttpResponse<IApprovalHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
