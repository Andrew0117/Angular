import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SubSink } from 'subsink';
import { User } from '../model/user';
import { JwtResponse } from '../model/jwtResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public showLoading: boolean = false;
  private subscriptions = new SubSink();

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    if (this.userService.isUserLoggedIn()) {
      this.router.navigateByUrl('/user/home');
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  onLogin(user: User): void {
    this.subscriptions.add(
      this.userService.login(user).subscribe(
        (response) => {
          this.userService.addTokenToCache((response.body as JwtResponse).token || 'Bad Token');
          this.router.navigateByUrl('/user/home');
          this.showLoading = false;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.showLoading = false;
        }
      ));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
