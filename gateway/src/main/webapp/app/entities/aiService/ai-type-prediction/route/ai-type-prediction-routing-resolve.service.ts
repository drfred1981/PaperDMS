import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAITypePrediction } from '../ai-type-prediction.model';
import { AITypePredictionService } from '../service/ai-type-prediction.service';

const aITypePredictionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAITypePrediction> => {
  const id = route.params.id;
  if (id) {
    return inject(AITypePredictionService)
      .find(id)
      .pipe(
        mergeMap((aITypePrediction: HttpResponse<IAITypePrediction>) => {
          if (aITypePrediction.body) {
            return of(aITypePrediction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aITypePredictionResolve;
