import {Component} from '@angular/core';
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {SupportTicket} from "../../services/models/support-ticket";
import {ClientSupportTicketService} from "../../services/services/client-support-ticket.service";
import {SupportTicketCardComponent} from "./components/support-ticket-card/support-ticket-card.component";

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [
    NewNavbarComponent,
    NgForOf,
    NgIf,
    SupportTicketCardComponent
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

}
