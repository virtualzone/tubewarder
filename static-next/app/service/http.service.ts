import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { RestResponse } from '../response/rest-response';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class HttpService {
    private headers = new Headers({'Content-Type': 'application/json'});
    private rsUrl: string = 'http://localhost:8080/rs/';

    constructor(private http: Http) {

    }

    post<T extends RestResponse>(rs: string, payload: Object): Promise<T> {
        return this.http
            .post(this.rsUrl + rs, JSON.stringify(payload), {headers: this.headers})
            .toPromise()
            .then(res => <T>res.json())
            .catch(this.handleError);
    }

    get<T extends RestResponse>(rs: string): Promise<T> {
        return this.http
            .get(this.rsUrl + rs)
            .toPromise()
            .then(res => <T>res.json())
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('Error in http handling', error);
        return Promise.reject(error.message || error);
    }
}