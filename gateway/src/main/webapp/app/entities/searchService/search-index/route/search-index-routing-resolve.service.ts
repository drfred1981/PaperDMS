import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISearchIndex } from '../search-index.model';
import { SearchIndexService } from '../service/search-index.service';

const searchIndexResolve = (route: ActivatedRouteSnapshot): Observable<null | ISearchIndex> => {
  const id = route.params.id;
  if (id) {
    return inject(SearchIndexService)
      .find(id)
      .pipe(
        mergeMap((searchIndex: HttpResponse<ISearchIndex>) => {
          if (searchIndex.body) {
            return of(searchIndex.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default searchIndexResolve;
