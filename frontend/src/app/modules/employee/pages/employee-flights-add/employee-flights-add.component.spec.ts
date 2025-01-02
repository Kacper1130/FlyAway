import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFlightsAddComponent } from './employee-flights-add.component';

describe('EmployeeFlightsAddComponent', () => {
  let component: EmployeeFlightsAddComponent;
  let fixture: ComponentFixture<EmployeeFlightsAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeFlightsAddComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeFlightsAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
