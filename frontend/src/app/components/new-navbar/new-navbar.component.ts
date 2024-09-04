import { Component } from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {TokenService} from "../../services/token/token.service";


@Component({
  selector: 'app-new-navbar',
  standalone: true,
  imports: [
    MatToolbar,
    MatIcon,
    MatToolbarModule,
    MatIconButton,
    RouterLink,
    MatButton,
    FormsModule,
    NgIf,
    MatAnchor,
    RouterLinkActive
  ],
  templateUrl: './new-navbar.component.html',
  styleUrl: './new-navbar.component.scss'
})
export class NewNavbarComponent {

  constructor(
    private tokenService: TokenService
  ) {
  }

  get Firstname(): string {
    return this.tokenService.getFirstname();
  }


  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }
  //
  // ngOnInit(): void {
  //   const elements = document.querySelectorAll('.large-button');
  //   elements.forEach(link => {
  //     if (window.location.href.endsWith(link.getAttribute('href') || '')) {
  //       link.classList.add('active');
  //     }
  //     link.addEventListener('click', () => {
  //       elements.forEach(l => l.classList.remove('active'));
  //       link.classList.add('active');
  //     });
  //   });
  // }

  protected readonly localStorage = localStorage;
}
