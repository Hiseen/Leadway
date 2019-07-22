import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninContainerComponent } from './components/platform-container/signin-container/signin-container.component';
import { MainPanelComponent } from './components/platform-container/main-panel/main-panel.component';
import { SignupContainerComponent } from './components/platform-container/signup-container/signup-container.component';

const routes: Routes = [
  { path: '', redirectTo: '/signin', pathMatch: 'full' },
  { path: 'signin', component: SigninContainerComponent },
  { path: 'main', component: MainPanelComponent },
  { path: 'signup', component: SignupContainerComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
