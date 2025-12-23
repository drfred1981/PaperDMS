import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtractedText } from '../extracted-text.model';
import { ExtractedTextService } from '../service/extracted-text.service';

const extractedTextResolve = (route: ActivatedRouteSnapshot): Observable<null | IExtractedText> => {
  const id = route.params.id;
  if (id) {
    return inject(ExtractedTextService)
      .find(id)
      .pipe(
        mergeMap((extractedText: HttpResponse<IExtractedText>) => {
          if (extractedText.body) {
            return of(extractedText.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default extractedTextResolve;
