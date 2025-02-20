import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DatePipe, NgClass, NgForOf} from "@angular/common";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {ChatMessage} from "../../../../services/models/chat-message";
import {Client} from "@stomp/stompjs";
import {ActivatedRoute} from "@angular/router";
import SockJS from "sockjs-client";
import {SupportTicketSummaryDto} from "../../../../services/models/support-ticket-summary-dto";
import {ClientSupportTicketService} from "../../../../services/services/client-support-ticket.service";

@Component({
  selector: 'app-support-chat',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DatePipe,
    FormsModule,
    NgForOf,
    NewNavbarComponent,
    NgClass
  ],
  templateUrl: './support-chat.component.html',
  styleUrl: './support-chat.component.scss'
})
export class SupportChatComponent implements OnInit {
  @ViewChild('messageContainer') messageContainer!: ElementRef;

  ticketInfo!: SupportTicketSummaryDto;
  ticketId: string;
  messages: ChatMessage[] = [];
  newMessage: string = '';
  private stompClient!: Client;

  isTicketActive: boolean = true;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly ticketService: ClientSupportTicketService
  ) {
    this.ticketId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadTicketInfo();
    this.loadChatHistory();
    this.scrollToBottom();
    this.connectWebSocket();
  }

  connectWebSocket() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    this.stompClient.onConnect = () => {
      this.stompClient.subscribe(`/topic/ticket/${this.ticketId}`, (message) => {
        const chatMessage: ChatMessage = JSON.parse(message.body);
        this.messages.push(chatMessage);
        setTimeout(() => this.scrollToBottom(), 0);
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('STOMP error:', frame);
    };

    this.stompClient.activate();
  }

  loadTicketInfo() {
    this.ticketService.getTicketSummary({ticketId: this.ticketId}).subscribe({
      next: (res) => {
        this.ticketInfo = res;
        this.isTicketActive = this.ticketInfo.status !== 'CLOSED';
      }
    })
  }

  loadChatHistory() {
    this.ticketService.getChatMessages({ticketId: this.ticketId}).subscribe({
      next: (res) => {
        this.messages = res;
      }
    })
  }

  sendMessage() {
    if (this.newMessage.trim() && this.stompClient.connected) {
      const message: ChatMessage = {
        ticketId: this.ticketId,
        senderType: 'CLIENT',
        content: this.newMessage,
        timestamp: new Date().toISOString()
      };
      this.stompClient.publish({
        destination: `/app/ticket/${this.ticketId}/send`,
        body: JSON.stringify(message)
      });
      this.newMessage = '';
    }
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const element = this.messageContainer.nativeElement;
      element.scrollTop = element.scrollHeight;
    }, 100);
  }

}
