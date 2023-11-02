import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  baseApi=environment.apiUrl;
  signupUrl='http://localhost:8080/user/signup';
  constructor(private httpClient: HttpClient) { }
  signup(data: any) {
    return this.httpClient.post(`${this.signupUrl}`, data,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
  })
  }
  forgotPassword(data: any) {
    return this.httpClient.post(`${this.baseApi}/user/forgotPassword`, data,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
  })
  }
  login(data: any) {
    return this.httpClient.post(`${this.baseApi}/user/login`, data,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
  })
  }
  checkToken(){
    return this.httpClient.get(this.baseApi+"/user/checkToken");
  }
  changePassword(data:any){
    return this.httpClient.post(`${this.baseApi}/user/changePassword`, data,{
      headers: new HttpHeaders().set('Content-Type', 'application/json')
  })
  }
  getUsers(){
    return this.httpClient.get(this.baseApi+"/user/get");
  }
  update(data: any){
    return this.httpClient.post(this.baseApi+"/user/update",data,{
      headers: new HttpHeaders().set('Content-Type','application/json')
    });
  }
}
