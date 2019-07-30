import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninContainerComponent } from './components/platform-container/signin-container/signin-container.component';
import { MainPanelComponent } from './components/platform-container/main-panel/main-panel.component';
import { SignupContainerComponent } from './components/platform-container/signup-container/signup-container.component';

import { LandingPageComponent } from './components/platform-container/main-panel/landing-page/landing-page.component';
import { BusinessDetailComponent } from './components/platform-container/main-panel/business-detail/business-detail.component';
import { BusinessSearchComponent } from './components/platform-container/main-panel/business-search/business-search.component';
import { LoginAuthGuard } from './guards/login-auth.guard';
import { AutoLoginGuard } from './guards/auto-login.guard';
import { InfoFormComponent } from './components/platform-container/signup-container/info-form/info-form.component';
import { VerifyAccountComponent } from './components/platform-container/signup-container/verify-account/verify-account.component';
import { AdminUploadComponent } from './components/platform-container/main-panel/admin-upload/admin-upload.component';

const routes: Routes = [
  { path: 'signin', component: SigninContainerComponent, canActivate: [AutoLoginGuard] },
  {
    path: '',
    component: MainPanelComponent,
    children: [
      { path: '', component: LandingPageComponent },
      { path: 'search', component: BusinessSearchComponent },
      { path: 'business/:name', component: BusinessDetailComponent },
      { path: 'uploadtask', component: AdminUploadComponent }
    ],
    canActivate: [LoginAuthGuard]
  },
  {
    path: 'signup',
    component: SignupContainerComponent,
    children: [
      { path: '', component: InfoFormComponent },
      { path: 'verification', component: VerifyAccountComponent },
      { path: '**', redirectTo: '', pathMatch: 'full'}
    ],
    canActivate: [AutoLoginGuard]},
  // redirect to main lobby if the path is invalid
  { path: '**', redirectTo: '', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [LoginAuthGuard, AutoLoginGuard]
})
export class AppRoutingModule { }
