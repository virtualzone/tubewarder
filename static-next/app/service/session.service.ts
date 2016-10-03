import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { AuthResponse } from '../response/auth-response';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class SessionService {
    constructor(private http: HttpService) {

    }

    login(username: string, password: string): void {
        let payload = {
            username: username,
            password: password
        };
        this.http.post('auth', payload)
            .then((res: AuthResponse) => {
                console.log("------------------> " + res.error);
                console.log("------------------> " + res.token);
                console.log("------------------> " + res.user);
            });
    }

    logout(): void {

    }
}