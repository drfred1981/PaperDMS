import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { TagPredictionService } from '../service/tag-prediction.service';
import { ITagPrediction } from '../tag-prediction.model';

const tagPredictionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITagPrediction> => {
  const id = route.params.id;
  if (id) {
    return inject(TagPredictionService)
      .find(id)
      .pipe(
        mergeMap((tagPrediction: HttpResponse<ITagPrediction>) => {
          if (tagPrediction.body) {
            return of(tagPrediction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tagPredictionResolve;
