import request from './axios'

export function login(data) {
  return request.post('/user/login', data)
}

export function register(data) {
  return request.post('/user/register', data)
}

export function getUserByUsername(username) {
  return request.get(`/user/search?username=${username}`)
}

export function getUserInfo() {
  return request.get('/user/current')
}

export function getUserById(id) {
  return request.get('/user/' + id)
}

export function updateUser(data) {
  return request.put('/user/update', data)
}

export function updatePassword(data) {
  return request.put('/user/password', data)
}

export function getCreditRadar() {
  return request.get('/user/credit-radar')
}

export function getProfileStats() {
  return request.get('/user/profile-stats')
}