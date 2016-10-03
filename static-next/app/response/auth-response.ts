import { RestResponse } from './rest-response';
import { User } from '../model/user';

export class AuthResponse extends RestResponse {
    token: string;
    user: User;
}