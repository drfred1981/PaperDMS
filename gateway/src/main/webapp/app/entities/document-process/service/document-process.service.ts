import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentProcess, NewDocumentProcess } from '../document-process.model';

export type PartialUpdateDocumentProcess = Partial<IDocumentProcess> & Pick<IDocumentProcess, 'id'>;

export type EntityResponseType = HttpResponse<IDocumentProcess>;
export type EntityArrayResponseType = HttpResponse<IDocumentProcess[]>;

@Injectable({ providedIn: 'root' })
export class DocumentProcessService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-processes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-processes/_search');

  create(documentProcess: NewDocumentProcess): Observable<EntityResponseType> {
    return this.http.post<IDocumentProcess>(this.resourceUrl, documentProcess, { observe: 'response' });
  }

  update(documentProcess: IDocumentProcess): Observable<EntityResponseType> {
    return this.http.put<IDocumentProcess>(`${this.resourceUrl}/${this.getDocumentProcessIdentifier(documentProcess)}`, documentProcess, {
      observe: 'response',
    });
  }

  partialUpdate(documentProcess: PartialUpdateDocumentProcess): Observable<EntityResponseType> {
    return this.http.patch<IDocumentProcess>(`${this.resourceUrl}/${this.getDocumentProcessIdentifier(documentProcess)}`, documentProcess, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentProcess>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentProcess[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentProcess[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IDocumentProcess[]>()], asapScheduler)));
  }

  getDocumentProcessIdentifier(documentProcess: Pick<IDocumentProcess, 'id'>): number {
    return documentProcess.id;
  }

  compareDocumentProcess(o1: Pick<IDocumentProcess, 'id'> | null, o2: Pick<IDocumentProcess, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentProcessIdentifier(o1) === this.getDocumentProcessIdentifier(o2) : o1 === o2;
  }

  addDocumentProcessToCollectionIfMissing<Type extends Pick<IDocumentProcess, 'id'>>(
    documentProcessCollection: Type[],
    ...documentProcessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentProcesses: Type[] = documentProcessesToCheck.filter(isPresent);
    if (documentProcesses.length > 0) {
      const documentProcessCollectionIdentifiers = documentProcessCollection.map(documentProcessItem =>
        this.getDocumentProcessIdentifier(documentProcessItem),
      );
      const documentProcessesToAdd = documentProcesses.filter(documentProcessItem => {
        const documentProcessIdentifier = this.getDocumentProcessIdentifier(documentProcessItem);
        if (documentProcessCollectionIdentifiers.includes(documentProcessIdentifier)) {
          return false;
        }
        documentProcessCollectionIdentifiers.push(documentProcessIdentifier);
        return true;
      });
      return [...documentProcessesToAdd, ...documentProcessCollection];
    }
    return documentProcessCollection;
  }
}
