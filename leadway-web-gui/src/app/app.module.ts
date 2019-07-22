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
import { SignupContainerComponent } from './components/platform-container/signup-container/signup-container.component';
import { FooterComponent } from './components/platform-container/signup-container/footer/footer.component';
import { InfoFormComponent } from './components/platform-container/signup-container/info-form/info-form.component';
import { TopBarComponent } from './components/platform-container/signup-container/top-bar/top-bar.component';
import { NavigationComponent } from './components/platform-container/main-panel/navigation/navigation.component';
import { BusinessSearchComponent } from './components/platform-container/main-panel/business-search/business-search.component';
import { BusinessDetailComponent } from './components/platform-container/main-panel/business-detail/business-detail.component';
import { LandingPageComponent } from './components/platform-container/main-panel/landing-page/landing-page.component';

@NgModule({
  declarations: [
    AppComponent,
    PlatformContainerComponent,
    SigninContainerComponent,
    MainPanelComponent,
    FooterComponent,
    InfoFormComponent,
    TopBarComponent,
    SignupContainerComponent,
    NavigationComponent,
    BusinessSearchComponent,
    BusinessDetailComponent,
    LandingPageComponent
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
