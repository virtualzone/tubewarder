import { Address } from './address';
import { KeyValue } from './key-value';
import { Attachment } from './attachment';

export class SendModel {
    token: string;
    template: string;
    channel: string;
    recipient: Address;
    model: KeyValue[];
    attachments: Attachment[];
    keyword: string;
    details: string;
    echo: boolean;
}