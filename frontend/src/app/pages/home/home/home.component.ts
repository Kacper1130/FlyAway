import { Component } from '@angular/core';
import {NavbarComponent} from "../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../components/new-navbar/new-navbar.component";
import {AiChatWidgetComponent} from "../../../components/ai-chat-widget/ai-chat-widget.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent,
    AiChatWidgetComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
