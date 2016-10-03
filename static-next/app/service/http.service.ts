import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class HttpService {
    private headers = new Headers({'Content-Type': 'application/json'});

    constructor(private http: Http) {

    }

    post(rs: string, payload: Object): Promise<any> {
        return this.http
            .post(rs, JSON.stringify(payload), {headers: this.headers})
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    get(rs: string): Promise<any> {
        return this.http
            .get(rs)
            .toPromise()
            .then(res => res.json())
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('Error in http handling', error);
        return Promise.reject(error.message || error);
    }
}