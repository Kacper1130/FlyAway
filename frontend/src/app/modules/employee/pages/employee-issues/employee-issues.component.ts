import {Component, OnInit} from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {NgForOf} from "@angular/common";
import {
  EmployeeSupportTicketCardComponent
} from "./employee-support-ticket-card/employee-support-ticket-card.component";
import {SupportTicket} from "../../../../services/models/support-ticket";
import {EmployeeSupportTicketService} from "../../../../services/services/employee-support-ticket.service";

@Component({
  selector: 'app-employee-issues',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    NgForOf,
    EmployeeSupportTicketCardComponent
  ],
  templateUrl: './employee-issues.component.html',
  styleUrl: './employee-issues.component.scss'
})
export class EmployeeIssuesComponent implements OnInit {

  tickets: SupportTicket[] = [];

  constructor(private readonly ticketService: EmployeeSupportTicketService) {
  }

  ngOnInit(): void {
    this.findAllTickets()
  }

  private findAllTickets() {
    this.ticketService.getTickets().subscribe({
      next: (res) => {
        this.tickets = res;
      }
    })
  }

}
