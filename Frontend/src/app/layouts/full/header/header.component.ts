import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChangePasswordComponent } from 'src/app/material-component/change-password/change-password.component';
import { ConfirmationComponent } from 'src/app/material-component/dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  role:any;

  constructor(private router:Router,
    private dialog:MatDialog
    ) {
  }
  logout(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message : 'Logout',
      confirmation : true
    };
    const dialogRef = this.dialog.open(ConfirmationComponent,dialogConfig);
    const sub=dialogRef.componentInstance.onEmitStatusChange.subscribe((response:any)=>
    {
      dialogRef.close();
      localStorage.clear();
      this.router.navigate(['/']);
    })
  }
  changePassword()
  {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "600px";
    this.dialog.open(ChangePasswordComponent,dialogConfig);
  }
}