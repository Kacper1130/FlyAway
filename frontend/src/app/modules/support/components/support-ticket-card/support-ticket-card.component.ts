import {Component, Input} from '@angular/core';
import {SupportTicket} from "../../../../services/models/support-ticket";
import {DatePipe, NgClass} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-support-ticket-card',
  standalone: true,
  imports: [
    NgClass,
    DatePipe
  ],
  templateUrl: './support-ticket-card.component.html',
  styleUrl: './support-ticket-card.component.scss'
})
export class SupportTicketCardComponent {
  @Input() ticket!: SupportTicket;

  constructor(private readonly router: Router) {
  }

  goToChatHistory() {
    this.router.navigate(['/support', this.ticket.id]);
  }
  createNewTicket() {
    // Implement your ticket creation logic here
    // This could open a modal or navigate to a new ticket form
  }

}
