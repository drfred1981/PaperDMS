import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISearchFacet } from '../search-facet.model';
import { SearchFacetService } from '../service/search-facet.service';

const searchFacetResolve = (route: ActivatedRouteSnapshot): Observable<null | ISearchFacet> => {
  const id = route.params.id;
  if (id) {
    return inject(SearchFacetService)
      .find(id)
      .pipe(
        mergeMap((searchFacet: HttpResponse<ISearchFacet>) => {
          if (searchFacet.body) {
            return of(searchFacet.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default searchFacetResolve;
