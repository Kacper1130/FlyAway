import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from "@angular/router";
import {NgClass, NgIf} from "@angular/common";
import { NewNavbarComponent } from "../new-navbar/new-navbar.component";
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-payment-status',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    NewNavbarComponent,
    NgClass
  ],
  templateUrl: './payment-status.component.html',
  styleUrl: './payment-status.component.scss',
  animations: [
    trigger('fadeSlideIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('0.4s cubic-bezier(0.4, 0, 0.2, 1)',
          style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class PaymentStatusComponent implements OnInit {
  status: 'success' | 'cancel' | null = null;
  statusMessages = {
    success: {
      title: 'Payment Successful',
      message: 'Your booking has been confirmed. You will receive a confirmation email shortly.',
      icon: 'check_circle'
    },
    cancel: {
      title: 'Payment Cancelled',
      message: 'Your payment was not completed. Please try again or contact support if you need assistance.',
      icon: 'cancel'
    }
  };

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    // Read from route data instead of queryParams
    this.route.data.subscribe(data => {
      this.status = data['status'];
    });
  }

  retryPayment(): void {
    window.history.back();
  }

}
