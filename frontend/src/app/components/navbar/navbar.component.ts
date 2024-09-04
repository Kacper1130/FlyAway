import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {TokenService} from "../../services/token/token.service";
import {MatButtonModule} from '@angular/material/button';
import {MatLabel} from "@angular/material/form-field";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    FormsModule,
    MatButtonModule,
    MatLabel
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

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

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });
  }

  protected readonly localStorage = localStorage;
}
