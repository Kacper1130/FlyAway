import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatAnchor, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatToolbar} from "@angular/material/toolbar";
import {PaginatorModule} from "primeng/paginator";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";
import {MatBadge} from "@angular/material/badge";
import {EmployeeSupportTicketService} from "../../../../services/services/employee-support-ticket.service";

@Component({
  selector: 'app-employee-navbar',
  standalone: true,
  imports: [
    FormsModule,
    MatIcon,
    MatIconButton,
    MatToolbar,
    PaginatorModule,
    RouterLink,
    RouterLinkActive,
    MatBadge,
    MatAnchor
  ],
  templateUrl: './employee-navbar.component.html',
  styleUrl: './employee-navbar.component.scss'
})
export class EmployeeNavbarComponent implements OnInit {
  ticketsNumber: number = 0;

  constructor(
    private readonly tokenService: TokenService,
    private readonly employeeService: EmployeeSupportTicketService
  ) {
  }

  ngOnInit() {
    this.loadTicketsNumber();
  }

  get Email(): string {
    return this.tokenService.getEmail();
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }

  private loadTicketsNumber() {
    this.employeeService.getActiveTicketsCount().subscribe({
      next: (res) => {
        this.ticketsNumber = res;
      }
    })
  }
}
