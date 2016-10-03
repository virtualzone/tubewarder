import { RestModel } from './rest-model';
import { UserGroup } from './user-group';
import { ChannelTemplate } from './channel-template';

export class Template extends RestModel {
    name: string;
    group: UserGroup;
    channelTemplates: ChannelTemplate[];
}