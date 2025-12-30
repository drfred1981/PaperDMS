import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INotificationTemplate, NewNotificationTemplate } from '../notification-template.model';

export type PartialUpdateNotificationTemplate = Partial<INotificationTemplate> & Pick<INotificationTemplate, 'id'>;

type RestOf<T extends INotificationTemplate | NewNotificationTemplate> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestNotificationTemplate = RestOf<INotificationTemplate>;

export type NewRestNotificationTemplate = RestOf<NewNotificationTemplate>;

export type PartialUpdateRestNotificationTemplate = RestOf<PartialUpdateNotificationTemplate>;

export type EntityResponseType = HttpResponse<INotificationTemplate>;
export type EntityArrayResponseType = HttpResponse<INotificationTemplate[]>;

@Injectable({ providedIn: 'root' })
export class NotificationTemplateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-templates', 'notificationservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/notification-templates/_search', 'notificationservice');

  create(notificationTemplate: NewNotificationTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTemplate);
    return this.http
      .post<RestNotificationTemplate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationTemplate: INotificationTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTemplate);
    return this.http
      .put<RestNotificationTemplate>(`${this.resourceUrl}/${this.getNotificationTemplateIdentifier(notificationTemplate)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationTemplate: PartialUpdateNotificationTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTemplate);
    return this.http
      .patch<RestNotificationTemplate>(`${this.resourceUrl}/${this.getNotificationTemplateIdentifier(notificationTemplate)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationTemplate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestNotificationTemplate[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<INotificationTemplate[]>()], asapScheduler)),
    );
  }

  getNotificationTemplateIdentifier(notificationTemplate: Pick<INotificationTemplate, 'id'>): number {
    return notificationTemplate.id;
  }

  compareNotificationTemplate(o1: Pick<INotificationTemplate, 'id'> | null, o2: Pick<INotificationTemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationTemplateIdentifier(o1) === this.getNotificationTemplateIdentifier(o2) : o1 === o2;
  }

  addNotificationTemplateToCollectionIfMissing<Type extends Pick<INotificationTemplate, 'id'>>(
    notificationTemplateCollection: Type[],
    ...notificationTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationTemplates: Type[] = notificationTemplatesToCheck.filter(isPresent);
    if (notificationTemplates.length > 0) {
      const notificationTemplateCollectionIdentifiers = notificationTemplateCollection.map(notificationTemplateItem =>
        this.getNotificationTemplateIdentifier(notificationTemplateItem),
      );
      const notificationTemplatesToAdd = notificationTemplates.filter(notificationTemplateItem => {
        const notificationTemplateIdentifier = this.getNotificationTemplateIdentifier(notificationTemplateItem);
        if (notificationTemplateCollectionIdentifiers.includes(notificationTemplateIdentifier)) {
          return false;
        }
        notificationTemplateCollectionIdentifiers.push(notificationTemplateIdentifier);
        return true;
      });
      return [...notificationTemplatesToAdd, ...notificationTemplateCollection];
    }
    return notificationTemplateCollection;
  }

  protected convertDateFromClient<T extends INotificationTemplate | NewNotificationTemplate | PartialUpdateNotificationTemplate>(
    notificationTemplate: T,
  ): RestOf<T> {
    return {
      ...notificationTemplate,
      createdDate: notificationTemplate.createdDate?.toJSON() ?? null,
      lastModifiedDate: notificationTemplate.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationTemplate: RestNotificationTemplate): INotificationTemplate {
    return {
      ...restNotificationTemplate,
      createdDate: restNotificationTemplate.createdDate ? dayjs(restNotificationTemplate.createdDate) : undefined,
      lastModifiedDate: restNotificationTemplate.lastModifiedDate ? dayjs(restNotificationTemplate.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationTemplate>): HttpResponse<INotificationTemplate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationTemplate[]>): HttpResponse<INotificationTemplate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
