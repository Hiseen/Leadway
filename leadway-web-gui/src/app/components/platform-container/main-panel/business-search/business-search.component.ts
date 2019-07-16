import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'leadway-business-search',
  templateUrl: './business-search.component.html',
  styleUrls: ['./business-search.component.scss']
})
export class BusinessSearchComponent implements OnInit {

  public currentQuery = '';
  public currentLocation = '';

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      console.log('Activate route event occured');
      console.log(params);

      this.currentQuery = params.get('query');
      this.currentLocation = params.get('location');
    });
  }

}
