import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './auth/service/auth.service';
import { Header } from './header/header';
import { SideMenu } from './side-menu/side-menu';

@Component({
  selector: 'app-root',
  imports: [Header, SideMenu, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('Code_Gap');

  protected authService = inject(AuthService);
  protected readonly isSideMenuOpen = signal(true);

  protected toggleSideMenu(): void {
    this.isSideMenuOpen.update((isOpen) => !isOpen);
  }

  protected closeSideMenu(): void {
    this.isSideMenuOpen.set(false);
  }

  ngOnInit(): void {  
    this.authService.initLogin();
  }
}
