import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import { AICorrespondentPredictionService } from '../service/ai-correspondent-prediction.service';

const aICorrespondentPredictionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAICorrespondentPrediction> => {
  const id = route.params.id;
  if (id) {
    return inject(AICorrespondentPredictionService)
      .find(id)
      .pipe(
        mergeMap((aICorrespondentPrediction: HttpResponse<IAICorrespondentPrediction>) => {
          if (aICorrespondentPrediction.body) {
            return of(aICorrespondentPrediction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aICorrespondentPredictionResolve;
