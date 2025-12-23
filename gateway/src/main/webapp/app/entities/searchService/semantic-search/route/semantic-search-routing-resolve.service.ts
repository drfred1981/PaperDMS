import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISemanticSearch } from '../semantic-search.model';
import { SemanticSearchService } from '../service/semantic-search.service';

const semanticSearchResolve = (route: ActivatedRouteSnapshot): Observable<null | ISemanticSearch> => {
  const id = route.params.id;
  if (id) {
    return inject(SemanticSearchService)
      .find(id)
      .pipe(
        mergeMap((semanticSearch: HttpResponse<ISemanticSearch>) => {
          if (semanticSearch.body) {
            return of(semanticSearch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default semanticSearchResolve;
