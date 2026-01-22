import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../core/service/user.service';
import { of } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let userServiceMock: jest.Mocked<UserService>;

  beforeEach(() => {
    userServiceMock = {
      register: jest.fn()
    } as unknown as jest.Mocked<UserService>;

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      providers: [
        { provide: UserService, useValue: userServiceMock }
      ]
    }).overrideComponent(RegisterComponent, {
      set: {
        providers: [
          { provide: UserService, useValue: userServiceMock }
        ]
      }
    });

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

// DEBUT Ajout ETAPE 4 TEST 2

it('should not submit if form is invalid', () => {
  component.registerForm.setValue({
    firstName: '',
    lastName: '',
    login: '',
    password: ''
  });

  const spy = jest.spyOn(userServiceMock, 'register');
  component.onSubmit();

  expect(spy).not.toHaveBeenCalled();
});


it('should expose form controls via getter', () => {
  const controls = component.form;
  expect(controls).toHaveProperty('firstName');
  expect(controls).toHaveProperty('lastName');
});


// Fin Ajout ETAPE 4 TEST 2


  it('should initialize form with required fields', () => {
    expect(component.registerForm.contains('firstName')).toBe(true);
    expect(component.registerForm.contains('lastName')).toBe(true);
    expect(component.registerForm.contains('login')).toBe(true);
    expect(component.registerForm.contains('password')).toBe(true);
  });

  it('should call userService.register on valid submit', () => {
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    component.registerForm.setValue({
      firstName: 'John',
      lastName: 'Doe',
      login: 'johndoe',
      password: '1234'
    });

    userServiceMock.register.mockReturnValue(of({}));

    component.onSubmit();

    expect(userServiceMock.register).toHaveBeenCalledWith({
      firstName: 'John',
      lastName: 'Doe',
      login: 'johndoe',
      password: '1234'
    });
    expect(window.alert).toHaveBeenCalledWith('SUCCESS!! :-)');
  });

  it('should reset form on onReset()', () => {
    component.registerForm.setValue({
      firstName: 'John',
      lastName: 'Doe',
      login: 'johndoe',
      password: '1234'
    });
    component.submitted = true;

    component.onReset();

    expect(component.submitted).toBe(false);
    expect(component.registerForm.value).toEqual({
      firstName: null,
      lastName: null,
      login: null,
      password: null
    });
  });
});
