import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AppComponent } from './app.component';
import { DashboardComponent } from './component/dashboard.component';
import { LoginComponent } from './component/login.component';

import { HttpService } from './service/http.service';
import { SessionService } from './service/session.service';

import { routing } from './app.routing';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    declarations: [ 
        AppComponent,
        DashboardComponent,
        LoginComponent
    ],
    providers: [
        HttpService,
        SessionService
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule { }