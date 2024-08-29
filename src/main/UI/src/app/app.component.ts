import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {HttpClient, HttpResponse,HttpHeaders} from "@angular/common/http";
import { Observable } from 'rxjs';
import {map} from "rxjs/operators";





@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css', '../styles.css']
})
export class AppComponent implements OnInit{

  constructor(private httpClient:HttpClient){}

  private baseURL:string='http://localhost:8080';

  private getUrl:string = this.baseURL + '/room/reservation/v1/';
  private postUrl:string = this.baseURL + '/room/reservation/v1';
  private welcomeUrl:string = this.baseURL + '/api/welcome';
  private timeZoneUrl:string = this.baseURL + '/api/timezone';
  public submitted!:boolean;
  roomsearch! : FormGroup;
  rooms! : Room[];
  request!:ReserveRoomRequest;
  currentCheckInVal!:string;
  currentCheckOutVal!:string;
  welcomeMessages!:string[];
  timeZones!:string[];

  getWelcome(): Observable<string> {
    return this.httpClient.get<string>(this.welcomeUrl,{responseType:'text' as 'json'});
  }

  getTimeZone(): Observable<string> {
    return this.httpClient.get(this.timeZoneUrl, {responseType:'text'});
  }

  ngOnInit(){
    this.roomsearch= new FormGroup({
      checkin: new FormControl(' '),
      checkout: new FormControl(' ')
    });

    //     this.rooms=ROOMS;


    const roomsearchValueChanges$ = this.roomsearch.valueChanges;

    // subscribe to the stream
    roomsearchValueChanges$.subscribe(x => {
      this.currentCheckInVal = x.checkin;
      this.currentCheckOutVal = x.checkout;
    });

    this.getWelcome().subscribe(
      (response) => {
        this.welcomeMessages = JSON.parse(response);
      }
    );

    this.getTimeZone().subscribe(
      (response) => {
        this.timeZones = JSON.parse(response);
      }
    );
  }


  onSubmit({value,valid}:{value:Roomsearch,valid:boolean}){
    this.getAll().subscribe(

      rooms => {console.log(Object.values(rooms)[0]);this.rooms=<Room[]>Object.values(rooms)[0]; }


    );
  }
  reserveRoom(value:string){
    this.request = new ReserveRoomRequest(value, this.currentCheckInVal, this.currentCheckOutVal);

    this.createReservation(this.request);
  }
  createReservation(body:ReserveRoomRequest) {
    let bodyString = JSON.stringify(body); // Stringify payload
    let headers = new Headers({'Content-Type': 'application/json'}); // ... Set content type to JSON
    // let options = new RequestOptions({headers: headers}); // Create a request option

    const options = {
      headers: new HttpHeaders().append('key', 'value'),

    }

    this.httpClient.post(this.postUrl, body, options)
      .subscribe(res => console.log(res));
  }

  /*mapRoom(response:HttpResponse<any>): Room[]{
    return response.body;
  }*/

  getAll(): Observable<any> {


    return this.httpClient.get(this.baseURL + '/room/reservation/v1?checkin='+ this.currentCheckInVal + '&checkout='+this.currentCheckOutVal, {responseType: 'json'});
  }

}



export interface Roomsearch{
  checkin:string;
  checkout:string;
}




export interface Room{
  id:string;
  roomNumber:string;
  price:string;
  links:string;

}
export class ReserveRoomRequest {
  roomId:string;
  checkin:string;
  checkout:string;

  constructor(roomId:string,
              checkin:string,
              checkout:string) {

    this.roomId = roomId;
    this.checkin = checkin;
    this.checkout = checkout;
  }
}

/*
var ROOMS: Room[]=[
  {
  "id": "13932123",
  "roomNumber" : "409",
  "price" :"20",
  "links" : ""
},
{
  "id": "139324444",
  "roomNumber" : "509",
  "price" :"30",
  "links" : ""
},
{
  "id": "139324888",
  "roomNumber" : "609",
  "price" :"40",
  "links" : ""
}
] */
