import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CustomNgMaterialModule } from './common/custom-ng-material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PlatformContainerComponent } from './components/platform-container/platform-container.component';
import { SigninContainerComponent } from './components/platform-container/signin-container/signin-container.component';
import { MainPanelComponent } from './components/platform-container/main-panel/main-panel.component';
import { HttpClientModule } from '@angular/common/http';
import { EnterUserInfoComponent } from './components/user-info-container/enter-user-info/enter-user-info.component';
import { TopBarComponent } from './components/user-info-container/top-bar/top-bar.component';
import { InfoFormComponent } from './components/user-info-container/info-form/info-form.component';
import { FooterComponent } from './components/user-info-container/footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    PlatformContainerComponent,
    SigninContainerComponent,
    MainPanelComponent,
    EnterUserInfoComponent,
    TopBarComponent,
    InfoFormComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    BrowserAnimationsModule,
    CustomNgMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
