import {Component, ElementRef, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DatePipe, NgClass, NgForOf} from "@angular/common";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {ChatMessage} from "../../../../services/models/chat-message";
import {Client} from "@stomp/stompjs";
import {ActivatedRoute} from "@angular/router";
import {EmployeeSupportTicketService} from "../../../../services/services/employee-support-ticket.service";
import SockJS from "sockjs-client";

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
export class SupportChatComponent {
  @ViewChild('messageContainer') messageContainer!: ElementRef;

  ticketId: string;
  messages: ChatMessage[] = [];
  newMessage: string = '';
  private stompClient!: Client;

  constructor(private route: ActivatedRoute, private ticketService: EmployeeSupportTicketService) {
    this.ticketId = this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.loadChatHistory();
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

  loadChatHistory() {
    this.ticketService.getChatMessages1({ticketId: this.ticketId}).subscribe({
      next: (res) => {
        this.messages = res;
      }
    })
  }

  sendMessage() {
    if (this.newMessage.trim() && this.stompClient.connected) {
      const message: ChatMessage = {
        ticketId: this.ticketId,
        senderId: 1,
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

  scrollToBottom() {
    const container = this.messageContainer.nativeElement;
    container.scrollTop = container.scrollHeight;
  }
}
