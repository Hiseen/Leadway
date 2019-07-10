import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'leadway-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  public userInitial = 'W';

  constructor() { }

  ngOnInit() {
  }

}
