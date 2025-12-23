import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILanguageDetection } from '../language-detection.model';
import { LanguageDetectionService } from '../service/language-detection.service';

const languageDetectionResolve = (route: ActivatedRouteSnapshot): Observable<null | ILanguageDetection> => {
  const id = route.params.id;
  if (id) {
    return inject(LanguageDetectionService)
      .find(id)
      .pipe(
        mergeMap((languageDetection: HttpResponse<ILanguageDetection>) => {
          if (languageDetection.body) {
            return of(languageDetection.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default languageDetectionResolve;
