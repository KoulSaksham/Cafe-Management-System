import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SignupComponent } from '../signup/signup.component';
import { ForgotPasswordComponent } from '../forgot-password/forgot-password.component';
import { LoginComponent } from '../login/login.component';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {


  constructor(
    private dialog:MatDialog,
    private userService:UserService,
    private router:Router
    ) { }

  ngOnInit(): void {
    this.userService.checkToken().subscribe((response:any)=>
    {
      this.router.navigate(['/cafe/dashboard']);
    },
    (error:any)=>{
      console.log("Error from Home Component.ts",error)
    })
  }

  handleSignupAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="50%";
    dialogConfig.height="50%";
    this.dialog.open(SignupComponent,dialogConfig);
  }
  handleForgotPasswordAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="35%";
    dialogConfig.height="35%";
    this.dialog.open(ForgotPasswordComponent,dialogConfig);
  }
  handleLoginAction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width="50%";
    dialogConfig.height="50%";
    this.dialog.open(LoginComponent,dialogConfig);
  }

}