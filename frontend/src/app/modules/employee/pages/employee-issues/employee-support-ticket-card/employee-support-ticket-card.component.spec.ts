import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSupportTicketCardComponent } from './employee-support-ticket-card.component';

describe('EmployeeSupportTicketCardComponent', () => {
  let component: EmployeeSupportTicketCardComponent;
  let fixture: ComponentFixture<EmployeeSupportTicketCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeSupportTicketCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeSupportTicketCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
