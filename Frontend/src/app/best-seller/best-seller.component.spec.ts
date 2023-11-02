import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BestSellerComponent } from './best-seller.component';

describe('BestSellerComponent', () => {
  let component: BestSellerComponent;
  let fixture: ComponentFixture<BestSellerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BestSellerComponent]
    });
    fixture = TestBed.createComponent(BestSellerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
