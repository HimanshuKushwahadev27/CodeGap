import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-side-menu',
  standalone: true,
  templateUrl: './side-menu.html',
  styleUrl: './side-menu.scss',
})
export class SideMenu {
  @Input() isOpen = false;
  @Output() readonly menuClosed = new EventEmitter<void>();

  protected closeMenu(): void {
    this.menuClosed.emit();
  }
}
