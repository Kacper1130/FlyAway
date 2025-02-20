/* tslint:disable */
/* eslint-disable */
export interface ChatMessage {
  content?: string;
  id?: string;
  senderId?: number;
  senderType?: 'CLIENT' | 'EMPLOYEE';
  ticketId?: string;
  timestamp?: string;
}
