import {Component} from '@angular/core';
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {SupportTicket} from "../../services/models/support-ticket";
import {ClientSupportTicketService} from "../../services/services/client-support-ticket.service";
import {SupportTicketCardComponent} from "./components/support-ticket-card/support-ticket-card.component";
import {MatButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [
    NewNavbarComponent,
    NgForOf,
    NgIf,
    SupportTicketCardComponent,
    MatButton,
    RouterLink
  ],
  templateUrl: './support.component.html',
  styleUrl: './support.component.scss'
})
export class SupportComponent {

  supportTickets: SupportTicket[] = [];

  constructor(private readonly supportTicketService: ClientSupportTicketService) {
  }

  ngOnInit() {
    this.getSupportTickets();
  }

  private getSupportTickets() {
    this.supportTicketService.getOwnTickets().subscribe({
      next: (respone) => {
        this.supportTickets = respone;
      }
    })
  }

  createNewTicket() {
    // Implement your ticket creation logic here
    // This could open a modal or navigate to a new ticket form
  }

}
