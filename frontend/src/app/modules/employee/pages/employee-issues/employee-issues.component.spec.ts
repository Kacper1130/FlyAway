import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeIssuesComponent } from './employee-issues.component';

describe('EmployeeIssuesComponent', () => {
  let component: EmployeeIssuesComponent;
  let fixture: ComponentFixture<EmployeeIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeIssuesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
