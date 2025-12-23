import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentComment } from '../document-comment.model';
import { DocumentCommentService } from '../service/document-comment.service';

const documentCommentResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentComment> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentCommentService)
      .find(id)
      .pipe(
        mergeMap((documentComment: HttpResponse<IDocumentComment>) => {
          if (documentComment.body) {
            return of(documentComment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentCommentResolve;
