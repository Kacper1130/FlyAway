import {Component, Input} from '@angular/core';
import {Aircraft} from "../../../../../services/models/aircraft";
import {NgForOf, NgIf} from "@angular/common";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-single-aircraft',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    MatIcon
  ],
  templateUrl: './single-aircraft.component.html',
  styleUrl: './single-aircraft.component.scss'
})
export class SingleAircraftComponent {
  @Input() aircraft!: Aircraft;
  objectKeys = Object.keys;

  getSeatsInClass(cabinClass: string): number {
    const range = this.aircraft.seatClassRanges[cabinClass];
    if (!range || range.startSeat == null || range.endSeat == null) {
      return 0;
    }
    return range.endSeat - range.startSeat + 1;
  }

  getSeatsRange(seatClass: string) {
    const range = this.aircraft.seatClassRanges[seatClass];
    if (!range) {
      return '-';
    }
    return `${range.startSeat} - ${range.endSeat}`;
  }
}
