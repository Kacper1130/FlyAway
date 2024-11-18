import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";

@Component({
  selector: 'app-employee-reservations',
  standalone: true,
    imports: [
        EmployeeNavbarComponent
    ],
  templateUrl: './employee-reservations.component.html',
  styleUrl: './employee-reservations.component.scss'
})
export class EmployeeReservationsComponent {

}
