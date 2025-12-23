import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentTemplate } from '../document-template.model';
import { DocumentTemplateService } from '../service/document-template.service';

const documentTemplateResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentTemplate> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentTemplateService)
      .find(id)
      .pipe(
        mergeMap((documentTemplate: HttpResponse<IDocumentTemplate>) => {
          if (documentTemplate.body) {
            return of(documentTemplate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentTemplateResolve;
