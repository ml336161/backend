import request from './axios'

export function signIn() {
  return request.post('/coin/sign-in')
}

export function getCoinLogs() {
  return request.get('/coin/logs')
}

export function checkSigned() {
  return request.get('/coin/signed')
}

export function getConsecutiveDays() {
  return request.get('/coin/consecutive-days')
}