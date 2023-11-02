import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewBillProductsComponent } from './view-bill-products.component';

describe('ViewBillProductsComponent', () => {
  let component: ViewBillProductsComponent;
  let fixture: ComponentFixture<ViewBillProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewBillProductsComponent]
    });
    fixture = TestBed.createComponent(ViewBillProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
