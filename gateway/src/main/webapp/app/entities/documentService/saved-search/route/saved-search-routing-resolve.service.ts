import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISavedSearch } from '../saved-search.model';
import { SavedSearchService } from '../service/saved-search.service';

const savedSearchResolve = (route: ActivatedRouteSnapshot): Observable<null | ISavedSearch> => {
  const id = route.params.id;
  if (id) {
    return inject(SavedSearchService)
      .find(id)
      .pipe(
        mergeMap((savedSearch: HttpResponse<ISavedSearch>) => {
          if (savedSearch.body) {
            return of(savedSearch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default savedSearchResolve;
