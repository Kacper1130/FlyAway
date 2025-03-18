import {Component, OnInit} from '@angular/core';
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";
import {ReservationCardComponent} from "../reservation-card/reservation-card.component";
import {NgForOf, NgIf} from "@angular/common";
import {ReservationSummaryClientDto} from "../../../services/models/reservation-summary-client-dto";
import {ClientReservationService} from "../../../services/services/client-reservation.service";
import {AiChatWidgetComponent} from "../../../components/ai-chat-widget/ai-chat-widget.component";

@Component({
  selector: 'app-reservations',
  standalone: true,
    imports: [
        NewNavbarComponent,
        ReservationCardComponent,
        NgIf,
        NgForOf,
        AiChatWidgetComponent
    ],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.scss'
})
export class ReservationsComponent implements OnInit {
  reservations: ReservationSummaryClientDto[] = [];

  constructor(private readonly reservationService: ClientReservationService) {
  }

  ngOnInit() {
    this.getActiveReservations();
  }

  private getActiveReservations() {
    this.reservationService.getActiveReservations().subscribe({
      next: (reservations) => {
        this.reservations = reservations;
      }
    })
  }

}
