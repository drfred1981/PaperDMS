import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAILanguageDetection } from '../ai-language-detection.model';
import { AILanguageDetectionService } from '../service/ai-language-detection.service';

const aILanguageDetectionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAILanguageDetection> => {
  const id = route.params.id;
  if (id) {
    return inject(AILanguageDetectionService)
      .find(id)
      .pipe(
        mergeMap((aILanguageDetection: HttpResponse<IAILanguageDetection>) => {
          if (aILanguageDetection.body) {
            return of(aILanguageDetection.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aILanguageDetectionResolve;
