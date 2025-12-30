import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmailImportEmailAttachment, NewEmailImportEmailAttachment } from '../email-import-email-attachment.model';

export type PartialUpdateEmailImportEmailAttachment = Partial<IEmailImportEmailAttachment> & Pick<IEmailImportEmailAttachment, 'id'>;

export type EntityResponseType = HttpResponse<IEmailImportEmailAttachment>;
export type EntityArrayResponseType = HttpResponse<IEmailImportEmailAttachment[]>;

@Injectable({ providedIn: 'root' })
export class EmailImportEmailAttachmentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-import-email-attachments', 'emailimportdocumentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/email-import-email-attachments/_search',
    'emailimportdocumentservice',
  );

  create(emailImportEmailAttachment: NewEmailImportEmailAttachment): Observable<EntityResponseType> {
    return this.http.post<IEmailImportEmailAttachment>(this.resourceUrl, emailImportEmailAttachment, { observe: 'response' });
  }

  update(emailImportEmailAttachment: IEmailImportEmailAttachment): Observable<EntityResponseType> {
    return this.http.put<IEmailImportEmailAttachment>(
      `${this.resourceUrl}/${this.getEmailImportEmailAttachmentIdentifier(emailImportEmailAttachment)}`,
      emailImportEmailAttachment,
      { observe: 'response' },
    );
  }

  partialUpdate(emailImportEmailAttachment: PartialUpdateEmailImportEmailAttachment): Observable<EntityResponseType> {
    return this.http.patch<IEmailImportEmailAttachment>(
      `${this.resourceUrl}/${this.getEmailImportEmailAttachmentIdentifier(emailImportEmailAttachment)}`,
      emailImportEmailAttachment,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmailImportEmailAttachment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmailImportEmailAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmailImportEmailAttachment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IEmailImportEmailAttachment[]>()], asapScheduler)));
  }

  getEmailImportEmailAttachmentIdentifier(emailImportEmailAttachment: Pick<IEmailImportEmailAttachment, 'id'>): number {
    return emailImportEmailAttachment.id;
  }

  compareEmailImportEmailAttachment(
    o1: Pick<IEmailImportEmailAttachment, 'id'> | null,
    o2: Pick<IEmailImportEmailAttachment, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getEmailImportEmailAttachmentIdentifier(o1) === this.getEmailImportEmailAttachmentIdentifier(o2) : o1 === o2;
  }

  addEmailImportEmailAttachmentToCollectionIfMissing<Type extends Pick<IEmailImportEmailAttachment, 'id'>>(
    emailImportEmailAttachmentCollection: Type[],
    ...emailImportEmailAttachmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emailImportEmailAttachments: Type[] = emailImportEmailAttachmentsToCheck.filter(isPresent);
    if (emailImportEmailAttachments.length > 0) {
      const emailImportEmailAttachmentCollectionIdentifiers = emailImportEmailAttachmentCollection.map(emailImportEmailAttachmentItem =>
        this.getEmailImportEmailAttachmentIdentifier(emailImportEmailAttachmentItem),
      );
      const emailImportEmailAttachmentsToAdd = emailImportEmailAttachments.filter(emailImportEmailAttachmentItem => {
        const emailImportEmailAttachmentIdentifier = this.getEmailImportEmailAttachmentIdentifier(emailImportEmailAttachmentItem);
        if (emailImportEmailAttachmentCollectionIdentifiers.includes(emailImportEmailAttachmentIdentifier)) {
          return false;
        }
        emailImportEmailAttachmentCollectionIdentifiers.push(emailImportEmailAttachmentIdentifier);
        return true;
      });
      return [...emailImportEmailAttachmentsToAdd, ...emailImportEmailAttachmentCollection];
    }
    return emailImportEmailAttachmentCollection;
  }
}
