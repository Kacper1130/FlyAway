import {Component, Input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {DisplayEmployeeDto} from "../../../../../services/models/display-employee-dto";
import {MatIcon} from "@angular/material/icon";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";

@Component({
  selector: 'app-employee-card',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    MatIcon,
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatCardTitle
  ],
  templateUrl: './employee-card.component.html',
  styleUrl: './employee-card.component.scss'
})
export class EmployeeCardComponent {
  @Input() employee!: DisplayEmployeeDto;

}
