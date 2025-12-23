import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtractedField } from '../extracted-field.model';
import { ExtractedFieldService } from '../service/extracted-field.service';

const extractedFieldResolve = (route: ActivatedRouteSnapshot): Observable<null | IExtractedField> => {
  const id = route.params.id;
  if (id) {
    return inject(ExtractedFieldService)
      .find(id)
      .pipe(
        mergeMap((extractedField: HttpResponse<IExtractedField>) => {
          if (extractedField.body) {
            return of(extractedField.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default extractedFieldResolve;
