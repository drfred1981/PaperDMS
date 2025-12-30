import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaSmartFolder } from '../meta-smart-folder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../meta-smart-folder.test-samples';

import { MetaSmartFolderService, RestMetaSmartFolder } from './meta-smart-folder.service';

const requireRestSample: RestMetaSmartFolder = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaSmartFolder Service', () => {
  let service: MetaSmartFolderService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaSmartFolder | IMetaSmartFolder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaSmartFolderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MetaSmartFolder', () => {
      const metaSmartFolder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaSmartFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaSmartFolder', () => {
      const metaSmartFolder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaSmartFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaSmartFolder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaSmartFolder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaSmartFolder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaSmartFolder', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addMetaSmartFolderToCollectionIfMissing', () => {
      it('should add a MetaSmartFolder to an empty array', () => {
        const metaSmartFolder: IMetaSmartFolder = sampleWithRequiredData;
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing([], metaSmartFolder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaSmartFolder);
      });

      it('should not add a MetaSmartFolder to an array that contains it', () => {
        const metaSmartFolder: IMetaSmartFolder = sampleWithRequiredData;
        const metaSmartFolderCollection: IMetaSmartFolder[] = [
          {
            ...metaSmartFolder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing(metaSmartFolderCollection, metaSmartFolder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaSmartFolder to an array that doesn't contain it", () => {
        const metaSmartFolder: IMetaSmartFolder = sampleWithRequiredData;
        const metaSmartFolderCollection: IMetaSmartFolder[] = [sampleWithPartialData];
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing(metaSmartFolderCollection, metaSmartFolder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaSmartFolder);
      });

      it('should add only unique MetaSmartFolder to an array', () => {
        const metaSmartFolderArray: IMetaSmartFolder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaSmartFolderCollection: IMetaSmartFolder[] = [sampleWithRequiredData];
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing(metaSmartFolderCollection, ...metaSmartFolderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaSmartFolder: IMetaSmartFolder = sampleWithRequiredData;
        const metaSmartFolder2: IMetaSmartFolder = sampleWithPartialData;
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing([], metaSmartFolder, metaSmartFolder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaSmartFolder);
        expect(expectedResult).toContain(metaSmartFolder2);
      });

      it('should accept null and undefined values', () => {
        const metaSmartFolder: IMetaSmartFolder = sampleWithRequiredData;
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing([], null, metaSmartFolder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaSmartFolder);
      });

      it('should return initial array if no MetaSmartFolder is added', () => {
        const metaSmartFolderCollection: IMetaSmartFolder[] = [sampleWithRequiredData];
        expectedResult = service.addMetaSmartFolderToCollectionIfMissing(metaSmartFolderCollection, undefined, null);
        expect(expectedResult).toEqual(metaSmartFolderCollection);
      });
    });

    describe('compareMetaSmartFolder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaSmartFolder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10831 };
        const entity2 = null;

        const compareResult1 = service.compareMetaSmartFolder(entity1, entity2);
        const compareResult2 = service.compareMetaSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10831 };
        const entity2 = { id: 1010 };

        const compareResult1 = service.compareMetaSmartFolder(entity1, entity2);
        const compareResult2 = service.compareMetaSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 10831 };
        const entity2 = { id: 10831 };

        const compareResult1 = service.compareMetaSmartFolder(entity1, entity2);
        const compareResult2 = service.compareMetaSmartFolder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
