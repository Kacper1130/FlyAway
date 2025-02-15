import {Component} from '@angular/core';
import {NavbarComponent} from "../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";
import {ReservationCardComponent} from "../reservation-card/reservation-card.component";
import {ReservationDetailsComponent} from "../reservation-details/reservation-details.component";
import {NgForOf, NgIf} from "@angular/common";
import {ReservationSummaryClientDto} from "../../../services/models/reservation-summary-client-dto";
import {ClientReservationService} from "../../../services/services/client-reservation.service";

@Component({
  selector: 'app-reservations',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent,
    ReservationCardComponent,
    ReservationDetailsComponent,
    NgIf,
    NgForOf
  ],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.scss'
})
export class ReservationsComponent {
  reservations: ReservationSummaryClientDto[] = [];

  constructor(private readonly reservationService: ClientReservationService) {}

  ngOnInit() {
    this.getReservations();
  }

  private getReservations() {
    this.reservationService.getOwnReservations().subscribe({
      next: (reservations) => {
        this.reservations = reservations;
      }
    })
  }

}
