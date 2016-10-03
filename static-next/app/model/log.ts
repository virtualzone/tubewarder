import { RestModel } from './rest-model';

export class Log extends RestModel {
    date: string;
    appToken: string;
    appTokenName: string;
    keyword: string;
    details: string;
    templateName: string;
    templateId: string;
    channelName: string;
    channelId: string;
    senderName: string;
    senderAddress: string;
    recipientName: string;
    recipientAddress: string;
    subject: string;
    content: string;
    queueId: string;
    status: string;
}