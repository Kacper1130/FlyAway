import {Component} from '@angular/core';
import {CurrencyPipe, DatePipe, NgClass, NgIf} from "@angular/common";
import {ReservationDto} from "../../../services/models/reservation-dto";
import {ActivatedRoute} from "@angular/router";
import {ReservationService} from "../../../services/services/reservation.service";
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";

@Component({
  selector: 'app-reservation-details',
  standalone: true,
  imports: [
    NgClass,
    DatePipe,
    CurrencyPipe,
    NewNavbarComponent,
    NgIf
  ],
  templateUrl: './reservation-details.component.html',
  styleUrl: './reservation-details.component.scss'
})
export class ReservationDetailsComponent {
  reservation: ReservationDto | null = null;
  error: string | null = null;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly reservationService: ReservationService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const reservationId = params.get('id');
      if (reservationId) {
        this.fetchReservationDetails(reservationId);
      }
    });
  }

  fetchReservationDetails(id: string): void {
    this.reservationService.getReservationDetails({id: id}).subscribe({
      next: (res) => {
        console.log(res);
        this.reservation = res;
      },
      error: (err) => {
        this.error = err.error.message;
        console.error('Error fetching reservation details', err);
      }
    });
  }

  get duration(): string {
    if (!this.reservation?.flightDto?.departureDate || !this.reservation?.flightDto?.arrivalDate) {
      return 'N/A';
    }

    const departure = new Date(this.reservation.flightDto.departureDate);
    const arrival = new Date(this.reservation.flightDto.arrivalDate);
    const diff = arrival.getTime() - departure.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    return `${hours}h ${minutes}m`;
  }

  cancelReservation() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.reservationService.cancelOwnReservation({id: id}).subscribe({
      next: () => {
        window.location.reload();
      }
    })
  }

}
