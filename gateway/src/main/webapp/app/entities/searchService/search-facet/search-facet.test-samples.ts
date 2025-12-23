import { ISearchFacet, NewSearchFacet } from './search-facet.model';

export const sampleWithRequiredData: ISearchFacet = {
  id: 13977,
  facetName: 'private',
  facetType: 'HIERARCHY',
  values: '../fake-data/blob/hipster.txt',
  counts: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ISearchFacet = {
  id: 23264,
  facetName: 'perfumed',
  facetType: 'HIERARCHY',
  values: '../fake-data/blob/hipster.txt',
  counts: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ISearchFacet = {
  id: 15995,
  searchQueryId: 3192,
  facetName: 'off onto accentuate',
  facetType: 'TERMS',
  values: '../fake-data/blob/hipster.txt',
  counts: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewSearchFacet = {
  facetName: 'unit',
  facetType: 'HIERARCHY',
  values: '../fake-data/blob/hipster.txt',
  counts: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
