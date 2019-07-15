import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninContainerComponent } from './components/platform-container/signin-container/signin-container.component';
import { MainPanelComponent } from './components/platform-container/main-panel/main-panel.component';
import { LandingPageComponent } from './components/platform-container/main-panel/landing-page/landing-page.component';
import { BusinessDetailComponent } from './components/platform-container/main-panel/business-detail/business-detail.component';
import { BusinessSearchComponent } from './components/platform-container/main-panel/business-search/business-search.component';
import { LoginAuthGuard } from './guards/login-auth.guard';

const routes: Routes = [
  { path: 'signin', component: SigninContainerComponent },
  {
    path: '',
    component: MainPanelComponent,
    children: [
      { path: '', component: LandingPageComponent },
      { path: 'search', component: BusinessSearchComponent },
      { path: 'business/:name', component: BusinessDetailComponent }
    ],
    canActivate: [LoginAuthGuard]
  },
  // redirect to main lobby if the path is invalid
  { path: '**', redirectTo: '', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [LoginAuthGuard]
})
export class AppRoutingModule { }
