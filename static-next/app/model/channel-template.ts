import { RestModel } from './rest-model';
import { Template } from './template';
import { Channel } from './channel';

export class ChannelTemplate extends RestModel {
    template: Template;
    channel: Channel;
    subject: string;
    content: string;
    senderAddress: string;
    senderName: string;
}
