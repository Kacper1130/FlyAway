import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";

@Component({
  selector: 'app-employee-panel',
  standalone: true,
  imports: [
    EmployeeNavbarComponent
  ],
  templateUrl: './employee-panel.component.html',
  styleUrl: './employee-panel.component.scss'
})
export class EmployeePanelComponent {

}
