import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailAttachment } from '../email-attachment.model';
import { EmailAttachmentService } from '../service/email-attachment.service';

const emailAttachmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailAttachment> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailAttachmentService)
      .find(id)
      .pipe(
        mergeMap((emailAttachment: HttpResponse<IEmailAttachment>) => {
          if (emailAttachment.body) {
            return of(emailAttachment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailAttachmentResolve;
