import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailImportEmailAttachment } from '../email-import-email-attachment.model';
import { EmailImportEmailAttachmentService } from '../service/email-import-email-attachment.service';

const emailImportEmailAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailImportEmailAttachment> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailImportEmailAttachmentService)
      .find(id)
      .pipe(
        mergeMap((emailImportEmailAttachment: HttpResponse<IEmailImportEmailAttachment>) => {
          if (emailImportEmailAttachment.body) {
            return of(emailImportEmailAttachment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailImportEmailAttachmentResolve;
