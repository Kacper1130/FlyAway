import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSupportChatComponent } from './employee-support-chat.component';

describe('EmployeeSupportChatComponent', () => {
  let component: EmployeeSupportChatComponent;
  let fixture: ComponentFixture<EmployeeSupportChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeSupportChatComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeSupportChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
