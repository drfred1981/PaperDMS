import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISearchQuery } from '../search-query.model';
import { SearchQueryService } from '../service/search-query.service';

const searchQueryResolve = (route: ActivatedRouteSnapshot): Observable<null | ISearchQuery> => {
  const id = route.params.id;
  if (id) {
    return inject(SearchQueryService)
      .find(id)
      .pipe(
        mergeMap((searchQuery: HttpResponse<ISearchQuery>) => {
          if (searchQuery.body) {
            return of(searchQuery.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default searchQueryResolve;
