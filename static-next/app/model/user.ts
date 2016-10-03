import { RestModel } from './rest-model';

export class User extends RestModel {
    displayName: string;
    username: string;
    password: string;
    enabled: boolean;
    lastLogin: string;
    allowAppTokens: boolean;
    allowChannels: boolean;
    allowTemplates: boolean;
    allowSystemConfig: boolean;
    allowLogs: boolean;
}
