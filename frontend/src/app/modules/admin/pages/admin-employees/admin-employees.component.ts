import {Component} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {RouterLink} from "@angular/router";
import {NgForOf} from "@angular/common";
import {SingleAircraftComponent} from "../aircraft-list/single-aircraft/single-aircraft.component";
import {EmployeeCardComponent} from "./employee-card/employee-card.component";
import {EmployeeService} from "../../../../services/services/employee.service";
import {DisplayEmployeeDto} from "../../../../services/models/display-employee-dto";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    RouterLink,
    NgForOf,
    SingleAircraftComponent,
    EmployeeCardComponent,
    MatButton
  ],
  templateUrl: './admin-employees.component.html',
  styleUrl: './admin-employees.component.scss'
})
export class AdminEmployeesComponent {
  employees: DisplayEmployeeDto[] = [];

  constructor(private readonly employeeService: EmployeeService) {
  }

  ngOnInit(): void {
    this.findAllEmployees();
  }

  private findAllEmployees() {
    this.employeeService.getAllEmployees().subscribe({
      next: (res) => {
        this.employees = res;
      }
    })
  }

}
