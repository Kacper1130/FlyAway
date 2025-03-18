import {Component, OnInit} from '@angular/core';
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {ReservationCardComponent} from "../reservation-card/reservation-card.component";
import {ReservationSummaryClientDto} from "../../../services/models/reservation-summary-client-dto";
import {ClientReservationService} from "../../../services/services/client-reservation.service";
import {AiChatWidgetComponent} from "../../../components/ai-chat-widget/ai-chat-widget.component";

@Component({
  selector: 'app-history',
  standalone: true,
    imports: [
        NewNavbarComponent,
        NgForOf,
        NgIf,
        ReservationCardComponent,
        AiChatWidgetComponent
    ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent implements OnInit {

  reservations: ReservationSummaryClientDto[] = [];

  constructor(private readonly reservationService: ClientReservationService) {}

  ngOnInit() {
    this.getReservationHistory();
  }

  private getReservationHistory() {
    this.reservationService.getReservationHistory().subscribe({
      next: (reservations) => {
        this.reservations = reservations;
      }
    })
  }

}
