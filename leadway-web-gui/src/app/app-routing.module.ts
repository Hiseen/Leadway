import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SigninContainerComponent } from './components/platform-container/signin-container/signin-container.component';
import { MainPanelComponent } from './components/platform-container/main-panel/main-panel.component';
import { EnterUserInfoComponent } from './components/user-info-container/enter-user-info/enter-user-info.component';

const routes: Routes = [
  { path: '', redirectTo: '/signin', pathMatch: 'full' },
  { path: 'signin', component: SigninContainerComponent },
  { path: 'main', component: MainPanelComponent },
  { path: 'signup', component: EnterUserInfoComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
