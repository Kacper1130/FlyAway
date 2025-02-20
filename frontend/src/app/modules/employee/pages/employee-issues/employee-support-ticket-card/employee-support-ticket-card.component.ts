import {Component, Input} from '@angular/core';
import {SupportTicket} from "../../../../../services/models/support-ticket";
import {DatePipe, NgClass} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-employee-support-ticket-card',
  standalone: true,
  imports: [
    DatePipe,
    NgClass
  ],
  templateUrl: './employee-support-ticket-card.component.html',
  styleUrl: './employee-support-ticket-card.component.scss'
})
export class EmployeeSupportTicketCardComponent {
  @Input() ticket!: SupportTicket;

  constructor(private readonly router: Router) {
  }

  onClick() {
    this.router.navigate(['/employee/issues', this.ticket.id]);
  }
}
