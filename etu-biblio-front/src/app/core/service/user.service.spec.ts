import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';
import { of } from 'rxjs';
import { Register } from '../models/Register';

describe('UserService', () => {
  let service: UserService;
  let httpClientMock: jest.Mocked<HttpClient>;

  beforeEach(() => {
    httpClientMock = {
      post: jest.fn()
    } as unknown as jest.Mocked<HttpClient>;

    TestBed.configureTestingModule({
      providers: [
        UserService,
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });

    service = TestBed.inject(UserService);
  });

  it('should call HttpClient.post with correct URL and body', () => {
    const mockUser: Register = { login: 'john', password: '1234' };
    const mockResponse = { success: true };

    httpClientMock.post.mockReturnValue(of(mockResponse));

    service.register(mockUser).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    expect(httpClientMock.post).toHaveBeenCalledWith('/api/register', mockUser);
  });
});
