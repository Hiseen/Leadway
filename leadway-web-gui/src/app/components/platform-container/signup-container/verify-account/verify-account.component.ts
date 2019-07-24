import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'leadway-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrls: ['./verify-account.component.scss']
})
export class VerifyAccountComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  public navigateToSignIn(): void {
    this.router.navigate(['signin']);
  }

}
