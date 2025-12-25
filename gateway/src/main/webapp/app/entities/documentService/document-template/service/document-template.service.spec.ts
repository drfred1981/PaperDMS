import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocumentTemplate } from '../document-template.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-template.test-samples';

import { DocumentTemplateService, RestDocumentTemplate } from './document-template.service';

const requireRestSample: RestDocumentTemplate = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('DocumentTemplate Service', () => {
  let service: DocumentTemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentTemplate | IDocumentTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentTemplateService);
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

    it('should create a DocumentTemplate', () => {
      const documentTemplate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentTemplate', () => {
      const documentTemplate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentTemplate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentTemplate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentTemplate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentTemplateToCollectionIfMissing', () => {
      it('should add a DocumentTemplate to an empty array', () => {
        const documentTemplate: IDocumentTemplate = sampleWithRequiredData;
        expectedResult = service.addDocumentTemplateToCollectionIfMissing([], documentTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentTemplate);
      });

      it('should not add a DocumentTemplate to an array that contains it', () => {
        const documentTemplate: IDocumentTemplate = sampleWithRequiredData;
        const documentTemplateCollection: IDocumentTemplate[] = [
          {
            ...documentTemplate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentTemplateToCollectionIfMissing(documentTemplateCollection, documentTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentTemplate to an array that doesn't contain it", () => {
        const documentTemplate: IDocumentTemplate = sampleWithRequiredData;
        const documentTemplateCollection: IDocumentTemplate[] = [sampleWithPartialData];
        expectedResult = service.addDocumentTemplateToCollectionIfMissing(documentTemplateCollection, documentTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentTemplate);
      });

      it('should add only unique DocumentTemplate to an array', () => {
        const documentTemplateArray: IDocumentTemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentTemplateCollection: IDocumentTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTemplateToCollectionIfMissing(documentTemplateCollection, ...documentTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentTemplate: IDocumentTemplate = sampleWithRequiredData;
        const documentTemplate2: IDocumentTemplate = sampleWithPartialData;
        expectedResult = service.addDocumentTemplateToCollectionIfMissing([], documentTemplate, documentTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentTemplate);
        expect(expectedResult).toContain(documentTemplate2);
      });

      it('should accept null and undefined values', () => {
        const documentTemplate: IDocumentTemplate = sampleWithRequiredData;
        expectedResult = service.addDocumentTemplateToCollectionIfMissing([], null, documentTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentTemplate);
      });

      it('should return initial array if no DocumentTemplate is added', () => {
        const documentTemplateCollection: IDocumentTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentTemplateToCollectionIfMissing(documentTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(documentTemplateCollection);
      });
    });

    describe('compareDocumentTemplate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25776 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentTemplate(entity1, entity2);
        const compareResult2 = service.compareDocumentTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25776 };
        const entity2 = { id: 10432 };

        const compareResult1 = service.compareDocumentTemplate(entity1, entity2);
        const compareResult2 = service.compareDocumentTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25776 };
        const entity2 = { id: 25776 };

        const compareResult1 = service.compareDocumentTemplate(entity1, entity2);
        const compareResult2 = service.compareDocumentTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
