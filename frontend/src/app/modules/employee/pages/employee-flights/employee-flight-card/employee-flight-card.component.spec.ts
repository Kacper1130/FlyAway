import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFlightCardComponent } from './employee-flight-card.component';

describe('EmployeeFlightCardComponent', () => {
  let component: EmployeeFlightCardComponent;
  let fixture: ComponentFixture<EmployeeFlightCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeFlightCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeFlightCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
