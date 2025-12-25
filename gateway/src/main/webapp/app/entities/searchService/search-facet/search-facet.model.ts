import { ISearchQuery } from 'app/entities/searchService/search-query/search-query.model';
import { FacetType } from 'app/entities/enumerations/facet-type.model';

export interface ISearchFacet {
  id: number;
  searchQueryId?: number | null;
  facetName?: string | null;
  facetType?: keyof typeof FacetType | null;
  values?: string | null;
  counts?: string | null;
  searchQuery?: Pick<ISearchQuery, 'id'> | null;
}

export type NewSearchFacet = Omit<ISearchFacet, 'id'> & { id: null };
