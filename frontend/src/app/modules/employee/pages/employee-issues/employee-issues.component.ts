import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";

@Component({
  selector: 'app-employee-issues',
  standalone: true,
    imports: [
        EmployeeNavbarComponent
    ],
  templateUrl: './employee-issues.component.html',
  styleUrl: './employee-issues.component.scss'
})
export class EmployeeIssuesComponent {

}
