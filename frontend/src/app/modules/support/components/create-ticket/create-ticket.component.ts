import {Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {ClientSupportTicketService} from "../../../../services/services/client-support-ticket.service";
import {CreateSupportTicketDto} from "../../../../services/models/create-support-ticket-dto";
import {AiChatWidgetComponent} from "../../../../components/ai-chat-widget/ai-chat-widget.component";

@Component({
  selector: 'app-create-ticket',
  standalone: true,
    imports: [
        FormsModule,
        NewNavbarComponent,
        ReactiveFormsModule,
        NgIf,
        AiChatWidgetComponent
    ],
  templateUrl: './create-ticket.component.html',
  styleUrl: './create-ticket.component.scss'
})
export class CreateTicketComponent {
  ticketForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly supportService: ClientSupportTicketService
  ) {
    this.ticketForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
      message: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  get title() {
    return this.ticketForm.get('title');
  }

  get message() {
    return this.ticketForm.get('message');
  }

  onSubmit() {
    const createTicketDto: CreateSupportTicketDto = {
      title: this.ticketForm.value.title,
      message: this.ticketForm.value.message
    };
    this.supportService.createTicket({body: createTicketDto}).subscribe({
      next: (res) => {
        console.log(res);
        this.router.navigate(['/support']);
      },
      error: (err) => {
        console.error('Error creating ticket: ', err);
      }
    })
  }

  cancel() {
    this.router.navigate(['/support']);
  }
}
