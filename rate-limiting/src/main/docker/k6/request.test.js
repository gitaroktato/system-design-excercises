import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

export default function () {
  for (let id = 1; id <= 8; id++) {
    const res = http.get(`http://localhost:8080/${__ENV.PATH}/key?id=key${id}&apiKey=${__ENV.API_KEY}`);
    check(res, {
      'is status 200': (r) => r.status === 200,
      'body is correct': (r) => r.body == `value${id}`
    });
    sleep(__ENV.SLEEP || 0)
  }
}