import http from 'k6/http';
import { check } from 'k6';

export default function () {
  for (let id = 1; id <= 3; id++) {
    const res = http.get(`http://localhost:8080/direct/key?id=key${id}&apiKey=api_one`);
    check(res, {
      'is status 200': (r) => r.status === 200,
      'body is correct': (r) => r.body == `value${id}`
    });
  }
}