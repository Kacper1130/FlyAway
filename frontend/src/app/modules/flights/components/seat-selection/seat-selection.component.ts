import {Component, Input, Output, EventEmitter, OnInit, SimpleChanges, OnChanges} from '@angular/core'; // Correct import
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {FlightService} from "../../../../services/services/flight.service";
import {AvailableSeatsDto} from "../../../../services/models/available-seats-dto";

@Component({
  selector: 'app-seat-selection',
  standalone: true,
  imports: [
    NgClass,
    NgForOf,
    NgIf
  ],
  templateUrl: './seat-selection.component.html',
  styleUrl: './seat-selection.component.scss'
})
export class SeatSelectionComponent implements OnInit, OnChanges{
  @Input() flightId!: string;
  @Input() selectedClass!: string;
  @Output() seatConfirmed = new EventEmitter<string>();

  seatingChart: { label: string; available: boolean }[][] = []; // Dynamically populated seating chart
  selectedSeat: { label: string; available: boolean } | null = null;

  constructor(private readonly flightService: FlightService) {}

  ngOnInit(): void {
    this.fetchSeats();
  }

  private mapAvailableSeatsToSeatingChart(availableSeats: { [key: string]: boolean }): { label: string; available: boolean }[][] {
    const seats: { label: string; available: boolean }[] = [];

    for (const [seatNumber, available] of Object.entries(availableSeats)) {
      seats.push({ label: seatNumber, available });
    }

    const rows: { label: string; available: boolean }[][] = [];
    for (let i = 0; i < seats.length; i += 6) {
      rows.push(seats.slice(i, i + 6)); // Rząd po 6 miejsc
    }

    for (let row of rows) {
      row.reverse();
    }

    return rows;
  }

  selectSeat(rowIndex: number, seatIndex: number): void {
    const seat = this.seatingChart[rowIndex][seatIndex];
    if (seat.available) {
      this.selectedSeat = seat;
    }
  }

  confirmSeatSelection(): void {
    if (this.selectedSeat) {
      this.seatConfirmed.emit(this.selectedSeat.label);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedClass'] && !changes['selectedClass'].firstChange) {
      this.fetchSeats(); // Pobierz miejsca dla nowej klasy
    }
  }

  private fetchSeats(): void {
    this.flightService.getAvailableSeats({ id: this.flightId, "cabin-class": this.selectedClass }).subscribe({
      next: (res: AvailableSeatsDto) => {
        if (res?.availableSeats) {
          this.seatingChart = this.mapAvailableSeatsToSeatingChart(res.availableSeats);
          this.selectedSeat = null; // Resetuj wybrane miejsce
        }
      },
      error: (err) => {
        console.error('Error fetching available seats:', err);
      }
    });
  }

}
