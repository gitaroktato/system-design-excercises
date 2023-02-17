import http from 'k6/http';

export default function () {
  http.get('http://localhost:8080/direct/key?id=key1');
}