import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAITagPrediction } from '../ai-tag-prediction.model';
import { AITagPredictionService } from '../service/ai-tag-prediction.service';

const aITagPredictionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAITagPrediction> => {
  const id = route.params.id;
  if (id) {
    return inject(AITagPredictionService)
      .find(id)
      .pipe(
        mergeMap((aITagPrediction: HttpResponse<IAITagPrediction>) => {
          if (aITagPrediction.body) {
            return of(aITagPrediction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aITagPredictionResolve;
