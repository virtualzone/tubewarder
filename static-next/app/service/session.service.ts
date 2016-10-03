import { Injectable } from '@angular/core';
import { HttpService } from './http.service';

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
        this.http.post('/rs/auth', payload)
            .then(res => {
                console.log("------------------> " + res);
            });
    }

    logout(): void {

    }
}