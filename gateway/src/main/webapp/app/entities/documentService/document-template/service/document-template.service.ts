import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentTemplate, NewDocumentTemplate } from '../document-template.model';

export type PartialUpdateDocumentTemplate = Partial<IDocumentTemplate> & Pick<IDocumentTemplate, 'id'>;

type RestOf<T extends IDocumentTemplate | NewDocumentTemplate> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentTemplate = RestOf<IDocumentTemplate>;

export type NewRestDocumentTemplate = RestOf<NewDocumentTemplate>;

export type PartialUpdateRestDocumentTemplate = RestOf<PartialUpdateDocumentTemplate>;

export type EntityResponseType = HttpResponse<IDocumentTemplate>;
export type EntityArrayResponseType = HttpResponse<IDocumentTemplate[]>;

@Injectable({ providedIn: 'root' })
export class DocumentTemplateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-templates', 'documentservice');

  create(documentTemplate: NewDocumentTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTemplate);
    return this.http
      .post<RestDocumentTemplate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentTemplate: IDocumentTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTemplate);
    return this.http
      .put<RestDocumentTemplate>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentTemplateIdentifier(documentTemplate))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentTemplate: PartialUpdateDocumentTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentTemplate);
    return this.http
      .patch<RestDocumentTemplate>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentTemplateIdentifier(documentTemplate))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentTemplate>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentTemplate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDocumentTemplateIdentifier(documentTemplate: Pick<IDocumentTemplate, 'id'>): number {
    return documentTemplate.id;
  }

  compareDocumentTemplate(o1: Pick<IDocumentTemplate, 'id'> | null, o2: Pick<IDocumentTemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentTemplateIdentifier(o1) === this.getDocumentTemplateIdentifier(o2) : o1 === o2;
  }

  addDocumentTemplateToCollectionIfMissing<Type extends Pick<IDocumentTemplate, 'id'>>(
    documentTemplateCollection: Type[],
    ...documentTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentTemplates: Type[] = documentTemplatesToCheck.filter(isPresent);
    if (documentTemplates.length > 0) {
      const documentTemplateCollectionIdentifiers = documentTemplateCollection.map(documentTemplateItem =>
        this.getDocumentTemplateIdentifier(documentTemplateItem),
      );
      const documentTemplatesToAdd = documentTemplates.filter(documentTemplateItem => {
        const documentTemplateIdentifier = this.getDocumentTemplateIdentifier(documentTemplateItem);
        if (documentTemplateCollectionIdentifiers.includes(documentTemplateIdentifier)) {
          return false;
        }
        documentTemplateCollectionIdentifiers.push(documentTemplateIdentifier);
        return true;
      });
      return [...documentTemplatesToAdd, ...documentTemplateCollection];
    }
    return documentTemplateCollection;
  }

  protected convertDateFromClient<T extends IDocumentTemplate | NewDocumentTemplate | PartialUpdateDocumentTemplate>(
    documentTemplate: T,
  ): RestOf<T> {
    return {
      ...documentTemplate,
      createdDate: documentTemplate.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentTemplate: RestDocumentTemplate): IDocumentTemplate {
    return {
      ...restDocumentTemplate,
      createdDate: restDocumentTemplate.createdDate ? dayjs(restDocumentTemplate.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentTemplate>): HttpResponse<IDocumentTemplate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentTemplate[]>): HttpResponse<IDocumentTemplate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
