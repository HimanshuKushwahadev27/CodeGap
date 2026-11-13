import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './auth/service/auth.service';
import { Header } from './header/header';

@Component({
  selector: 'app-root',
  imports: [Header, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('Code_Gap');

  protected authService = inject(AuthService);

  ngOnInit(): void {  
    this.authService.initLogin();
  }
}
