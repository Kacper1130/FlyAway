import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";

@Component({
  selector: 'app-employee-flights',
  standalone: true,
    imports: [
        EmployeeNavbarComponent
    ],
  templateUrl: './employee-flights.component.html',
  styleUrl: './employee-flights.component.scss'
})
export class EmployeeFlightsComponent {

}
