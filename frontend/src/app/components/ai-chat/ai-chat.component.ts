import {AfterViewChecked, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {MatFormField} from "@angular/material/form-field";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {AiChatService} from "../../services/services/ai-chat.service";
import {MatFabButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

interface ChatMessage {
  sender: 'user' | 'ai';
  content: string | SafeHtml;  // Update to accept SafeHtml
  timestamp: Date;
}

@Component({
  selector: 'app-ai-chat',
  standalone: true,
  imports: [
    MatIcon,
    NgIf,
    NgForOf,
    MatFormField,
    ReactiveFormsModule,
    CdkTextareaAutosize,
    DatePipe,
    NgClass,
    MatInput,
    MatFabButton
  ],
  templateUrl: './ai-chat.component.html',
  styleUrl: './ai-chat.component.scss'
})
export class AiChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messageInput = new FormControl('');
  messages: ChatMessage[] = [];
  isTyping = false;
  isLoading = false;

  constructor(private readonly aiChatService: AiChatService, private readonly sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.loadChatHistory();
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {
    }
  }

  sendMessage(): void {
    const message = this.messageInput.value?.trim();
    if (!message || this.isLoading) return;

    this.addMessage('user', message);
    this.messageInput.setValue('');

    this.isTyping = true;
    this.isLoading = true;
    console.log(message);
    this.aiChatService.useChat({body: {message: message}}).subscribe({
      next: (response) => {
        this.isTyping = false;
        this.isLoading = false;

        const processedMessage = this.processMessageWithLinks(response.message!);

        this.addMessage('ai', processedMessage);
        this.saveChatHistory();
      }
    });
  }

  processMessageWithLinks(content: string): SafeHtml {
    const urlRegex = /(https?:\/\/[^\s,]+)/g;

    const htmlContent = content.replace(urlRegex, (url) => {
      return `<a href="${url}" target="_blank">${url}</a>`;
    });

    return this.sanitizer.bypassSecurityTrustHtml(htmlContent);
  }

  addMessage(sender: 'user' | 'ai', content: string | SafeHtml): void {
    this.messages.push({
      sender,
      content,
      timestamp: new Date()
    });
  }

  private saveChatHistory(): void {
    try {
      // Convert SafeHtml back to string for storage
      const historyToSave = this.messages.map(msg => {
        let contentToSave = msg.content;

        // If it's SafeHtml, we need to extract the original text
        if (typeof contentToSave !== 'string') {
          // Get the element's innerHTML representation
          const div = document.createElement('div');
          div.innerHTML = contentToSave.toString();

          // For AI messages with links, store the original text
          if (msg.sender === 'ai') {
            // Get the text content without HTML tags
            contentToSave = div.textContent || div.innerText || '';
          } else {
            contentToSave = contentToSave.toString();
          }
        }

        return {
          sender: msg.sender,
          content: contentToSave,
          timestamp: msg.timestamp
        };
      });

      localStorage.setItem('chatHistory', JSON.stringify(historyToSave));
    } catch (error) {
      console.error('Error saving chat history:', error);
    }
  }

  private loadChatHistory(): void {
    try {
      const history = localStorage.getItem('chatHistory');
      if (history) {
        const parsedHistory = JSON.parse(history);

        this.messages = parsedHistory.map((msg: any) => {
          let content: string | SafeHtml = msg.content;

          // If the content appears to be a sanitized HTML string that was stored
          if (typeof content === 'string' &&
            (content.includes('SafeValue must use') ||
              content.includes('http://') ||
              content.includes('https://'))) {
            content = this.processMessageWithLinks(this.extractOriginalText(content));
          }

          return {
            sender: msg.sender,
            content: content,
            timestamp: new Date(msg.timestamp)
          };
        });
      }
    } catch (error) {
      console.error('Error loading chat history:', error);
      localStorage.removeItem('chatHistory');
    }
  }

  private extractOriginalText(safeHtmlString: string): string {
    const match = safeHtmlString.match(/SafeValue must use \[property\]=binding: (.*?) \(see/);
    if (match && match[1]) {
      return match[1];
    }

    return safeHtmlString.replace(/<[^>]*>/g, '');
  }

  handleKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

}
