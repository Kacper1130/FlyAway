import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatToolbar} from "@angular/material/toolbar";
import {NgIf} from "@angular/common";
import {PaginatorModule} from "primeng/paginator";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-admin-navbar',
  standalone: true,
    imports: [
        FormsModule,
        MatAnchor,
        MatButton,
        MatIcon,
        MatIconButton,
        MatToolbar,
        NgIf,
        PaginatorModule,
        RouterLink,
        RouterLinkActive
    ],
  templateUrl: './admin-navbar.component.html',
  styleUrl: './admin-navbar.component.scss'
})
export class AdminNavbarComponent {
  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }
}
