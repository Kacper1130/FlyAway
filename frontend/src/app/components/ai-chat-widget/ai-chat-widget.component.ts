import {Component} from '@angular/core';
import {AiChatComponent} from "../ai-chat/ai-chat.component";
import {MatFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatTooltip} from "@angular/material/tooltip";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-ai-chat-widget',
  standalone: true,
  imports: [
    AiChatComponent,
    MatFabButton,
    MatIcon,
    MatTooltip,
    NgIf
  ],
  templateUrl: './ai-chat-widget.component.html',
  styleUrl: './ai-chat-widget.component.scss'
})
export class AiChatWidgetComponent {
  isChatOpen = false;

  toggleChat() {
    this.isChatOpen = !this.isChatOpen;
  }
}
