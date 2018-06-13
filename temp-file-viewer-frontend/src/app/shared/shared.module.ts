import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatButtonModule,
  MatIconModule,
  MatSidenavModule,
  MatToolbarModule
} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
  ],
  exports: [
    CommonModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
  ],
  declarations: []
})
/**
 * 保存共享库
 */
export class SharedModule { }