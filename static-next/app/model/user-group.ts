import { RestModel } from './rest-model';
import { User } from './user';

export class UserGroup extends RestModel {
    name: string;
    members: User[];
}
