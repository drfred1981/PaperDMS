import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrcExtractedText } from '../orc-extracted-text.model';
import { OrcExtractedTextService } from '../service/orc-extracted-text.service';

const orcExtractedTextResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrcExtractedText> => {
  const id = route.params.id;
  if (id) {
    return inject(OrcExtractedTextService)
      .find(id)
      .pipe(
        mergeMap((orcExtractedText: HttpResponse<IOrcExtractedText>) => {
          if (orcExtractedText.body) {
            return of(orcExtractedText.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default orcExtractedTextResolve;
