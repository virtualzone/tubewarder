import { RestModel } from './rest-model';
import { UserGroup } from './user-group';

export class Channel extends RestModel {
    name: string;
    group: UserGroup;
    rewriteRecipientName: string;
    rewriteRecipientAddress: string;
    rewriteSubject: string;
    rewriteContent: string;
    config: Object;
    outputHandlerReadableName: string;
}