import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 10,         // количество виртуальных пользователей
    duration: '10s', // длительность теста
};

export default function () {
    let res = http.get('http://localhost:8080/');
    check(res, { 'status is 200': (r) => r.status === 200 });
}