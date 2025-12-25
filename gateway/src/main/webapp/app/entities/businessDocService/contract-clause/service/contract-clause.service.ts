import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractClause, NewContractClause } from '../contract-clause.model';

export type PartialUpdateContractClause = Partial<IContractClause> & Pick<IContractClause, 'id'>;

export type EntityResponseType = HttpResponse<IContractClause>;
export type EntityArrayResponseType = HttpResponse<IContractClause[]>;

@Injectable({ providedIn: 'root' })
export class ContractClauseService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-clauses', 'businessdocservice');

  create(contractClause: NewContractClause): Observable<EntityResponseType> {
    return this.http.post<IContractClause>(this.resourceUrl, contractClause, { observe: 'response' });
  }

  update(contractClause: IContractClause): Observable<EntityResponseType> {
    return this.http.put<IContractClause>(`${this.resourceUrl}/${this.getContractClauseIdentifier(contractClause)}`, contractClause, {
      observe: 'response',
    });
  }

  partialUpdate(contractClause: PartialUpdateContractClause): Observable<EntityResponseType> {
    return this.http.patch<IContractClause>(`${this.resourceUrl}/${this.getContractClauseIdentifier(contractClause)}`, contractClause, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContractClause>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractClause[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContractClauseIdentifier(contractClause: Pick<IContractClause, 'id'>): number {
    return contractClause.id;
  }

  compareContractClause(o1: Pick<IContractClause, 'id'> | null, o2: Pick<IContractClause, 'id'> | null): boolean {
    return o1 && o2 ? this.getContractClauseIdentifier(o1) === this.getContractClauseIdentifier(o2) : o1 === o2;
  }

  addContractClauseToCollectionIfMissing<Type extends Pick<IContractClause, 'id'>>(
    contractClauseCollection: Type[],
    ...contractClausesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contractClauses: Type[] = contractClausesToCheck.filter(isPresent);
    if (contractClauses.length > 0) {
      const contractClauseCollectionIdentifiers = contractClauseCollection.map(contractClauseItem =>
        this.getContractClauseIdentifier(contractClauseItem),
      );
      const contractClausesToAdd = contractClauses.filter(contractClauseItem => {
        const contractClauseIdentifier = this.getContractClauseIdentifier(contractClauseItem);
        if (contractClauseCollectionIdentifiers.includes(contractClauseIdentifier)) {
          return false;
        }
        contractClauseCollectionIdentifiers.push(contractClauseIdentifier);
        return true;
      });
      return [...contractClausesToAdd, ...contractClauseCollection];
    }
    return contractClauseCollection;
  }
}
