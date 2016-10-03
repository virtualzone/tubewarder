import { Component, Input } from '@angular/core';
import { SessionService } from '../service/session.service';
import { User } from '../model/user';

@Component({
    providers: [
        SessionService
    ],
    templateUrl: './app/component/login.component.html'
})
export class LoginComponent {
    user: User = new User();

    constructor(private sessionService: SessionService) {

    }

    onSubmit(): void {
        this.sessionService
            .login(this.user.username, this.user.password)
            .then();
    }

    onPasswordChange(): void {
        // TODO
    }
}