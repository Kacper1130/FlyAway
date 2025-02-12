import { Component } from '@angular/core';
import {NavbarComponent} from "../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";
import {ReservationCardComponent} from "../reservation-card/reservation-card.component";
import {ReservationDto} from "../../../services/models/reservation-dto";
import {ReservationService} from "../../../services/services/reservation.service";
import {ReservationDetailsComponent} from "../reservation-details/reservation-details.component";
import {NgForOf, NgIf} from "@angular/common";

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
  reservations: ReservationDto[] = [];
  selectedReservation: ReservationDto | null = null;
  showDetailView = false;

  constructor(private readonly reservationService: ReservationService) {}

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

  onCardClick(reservation: ReservationDto): void {
    this.selectedReservation = reservation;
    this.showDetailView = true;
  }

  onBackToList(): void {
    this.showDetailView = false;
    this.selectedReservation = null;
  }


}
