import request from './axios'

export function getActiveTypes() {
  return request.get('/skill-types/active')
}

export function getAllTypes() {
  return request.get('/skill-types')
}

export function getTypeById(id) {
  return request.get('/skill-types/' + id)
}