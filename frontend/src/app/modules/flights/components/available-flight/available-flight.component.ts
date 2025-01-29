import {Component, inject, Input} from '@angular/core';
import {FlightDto} from "../../../../services/models/flight-dto";
import {DatePipe, NgClass, NgIf} from "@angular/common";
import {SeatSelectionComponent} from "../seat-selection/seat-selection.component";
import {ReservationService} from "../../../../services/services/reservation.service";
import {CreateReservationDto} from "../../../../services/models/create-reservation-dto";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-available-flight',
  standalone: true,
  imports: [
    DatePipe,
    NgClass,
    SeatSelectionComponent,
    NgIf
  ],
  templateUrl: './available-flight.component.html',
  styleUrl: './available-flight.component.scss'
})
export class AvailableFlightComponent {
  @Input() flight!: FlightDto;

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';
  constructor(private readonly reservationService: ReservationService) {
  }

  get flightDuration(): string {
    if (!this.flight) {
      return '';
    }

    const departure = new Date(this.flight.departureDate);
    const arrival = new Date(this.flight.arrivalDate);

    const durationMs = arrival.getTime() - departure.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));

    return `${hours}h ${minutes}m`;
  }

  selectedClass: string | null = null;

  selectClass(cabinClass: string): void {
    this.selectedClass = cabinClass;
  }

  onSeatConfirmed(seatLabel: string): void {
    const params: CreateReservationDto = {
      cabinClass: this.selectedClass as 'FIRST' | 'BUSINESS' | 'ECONOMY',
      flightId: this.flight.id,
      seatNumber: parseInt(seatLabel),
    };
    this.reservationService.createReservation({ body: params }).subscribe({
      next: (response ) => {
        window.location.href = response.paymentUrl!;
        // const message = `Reservation created successfully`
        // this._snackBar.open(message, 'close', {
        //   horizontalPosition: this.horizontalPosition,
        //   verticalPosition: this.verticalPosition,
        //   panelClass: ['snackbar-success'],
        //   duration: 5000
        // });
        this.selectedClass = null;
      },

    });
  }

}
