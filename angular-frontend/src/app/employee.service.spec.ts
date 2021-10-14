import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { EmployeeService } from './employee.service';
import { Employee } from './employee';

describe('EmployeeService', () => {
  let httpClient:HttpClient;
  let httpTestingController: HttpTestingController;
  let empService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports:[HttpClientTestingModule],providers:[EmployeeService]});
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    empService = TestBed.inject(EmployeeService);

  });
  afterEach(()=>{
    httpTestingController.verify();
  });
  
  describe('#getAllEmployees', () => {
    let expectedEmps: Employee[];

    beforeEach(() => {
      //Dummy data to be returned by request.
      expectedEmps = [
        { id: 5, firstName: 'challa',lastName:"naveen",emailId:"prveen@gmail.com" },
        { id: 9, firstName: 'string',lastName:"string",emailId:"ram@gmail.com" },
      ] as Employee[];
    });
    
    //Test case 1
    it('should return expected employees by calling once', () => {
      empService.getEmployeesList().subscribe(
        emps => expect(emps).toEqual(expectedEmps, 'should return expected employees'),
        fail
      );

      const req = httpTestingController.expectOne(empService.empUrl);
      expect(req.request.method).toEqual('GET');

      req.flush(expectedEmps); //Return expectedEmps
    });
    
    //Test case 2
    it('should be OK returning no employee', () => {
      empService.getEmployeesList().subscribe(
        emps => expect(emps.length).toEqual(0, 'should have empty employee array'),
        fail
      );

      const req = httpTestingController.expectOne(empService.empUrl);
      req.flush([]); //Return empty data
    });
    
   

    //Test case 3
    it('should return expected employees when called multiple times', () => {
      empService.getEmployeesList().subscribe();
      empService.getEmployeesList().subscribe(
        emps => expect(emps).toEqual(expectedEmps, 'should return expected employees'),
        fail
      );

      const requests = httpTestingController.match(empService.empUrl);
      expect(requests.length).toEqual(2, 'calls to getAllEmployees()');

      requests[0].flush([]); //Return Empty body for first call
      requests[1].flush(expectedEmps); //Return expectedEmps in second call
    });

  //   it('posting an employee',()=>{
  //     empService.createEmployee(expectedEmps[0]).subscribe();
  //     empService.createEmployee(expectedEmps[0]).subscribe(emp=>expect(emp).toEqual(expectedEmps[0],'should retun expected'));
  //  })
  });
});