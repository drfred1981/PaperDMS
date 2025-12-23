import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentComment, NewDocumentComment } from '../document-comment.model';

export type PartialUpdateDocumentComment = Partial<IDocumentComment> & Pick<IDocumentComment, 'id'>;

type RestOf<T extends IDocumentComment | NewDocumentComment> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentComment = RestOf<IDocumentComment>;

export type NewRestDocumentComment = RestOf<NewDocumentComment>;

export type PartialUpdateRestDocumentComment = RestOf<PartialUpdateDocumentComment>;

export type EntityResponseType = HttpResponse<IDocumentComment>;
export type EntityArrayResponseType = HttpResponse<IDocumentComment[]>;

@Injectable({ providedIn: 'root' })
export class DocumentCommentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-comments', 'documentservice');

  create(documentComment: NewDocumentComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentComment);
    return this.http
      .post<RestDocumentComment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentComment: IDocumentComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentComment);
    return this.http
      .put<RestDocumentComment>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentCommentIdentifier(documentComment))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentComment: PartialUpdateDocumentComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentComment);
    return this.http
      .patch<RestDocumentComment>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentCommentIdentifier(documentComment))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentComment>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentComment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDocumentCommentIdentifier(documentComment: Pick<IDocumentComment, 'id'>): number {
    return documentComment.id;
  }

  compareDocumentComment(o1: Pick<IDocumentComment, 'id'> | null, o2: Pick<IDocumentComment, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentCommentIdentifier(o1) === this.getDocumentCommentIdentifier(o2) : o1 === o2;
  }

  addDocumentCommentToCollectionIfMissing<Type extends Pick<IDocumentComment, 'id'>>(
    documentCommentCollection: Type[],
    ...documentCommentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentComments: Type[] = documentCommentsToCheck.filter(isPresent);
    if (documentComments.length > 0) {
      const documentCommentCollectionIdentifiers = documentCommentCollection.map(documentCommentItem =>
        this.getDocumentCommentIdentifier(documentCommentItem),
      );
      const documentCommentsToAdd = documentComments.filter(documentCommentItem => {
        const documentCommentIdentifier = this.getDocumentCommentIdentifier(documentCommentItem);
        if (documentCommentCollectionIdentifiers.includes(documentCommentIdentifier)) {
          return false;
        }
        documentCommentCollectionIdentifiers.push(documentCommentIdentifier);
        return true;
      });
      return [...documentCommentsToAdd, ...documentCommentCollection];
    }
    return documentCommentCollection;
  }

  protected convertDateFromClient<T extends IDocumentComment | NewDocumentComment | PartialUpdateDocumentComment>(
    documentComment: T,
  ): RestOf<T> {
    return {
      ...documentComment,
      createdDate: documentComment.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentComment: RestDocumentComment): IDocumentComment {
    return {
      ...restDocumentComment,
      createdDate: restDocumentComment.createdDate ? dayjs(restDocumentComment.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentComment>): HttpResponse<IDocumentComment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentComment[]>): HttpResponse<IDocumentComment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
