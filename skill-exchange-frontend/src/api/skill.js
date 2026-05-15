import request from './axios'

export function getSkillList(params) {
  return request.get('/skills', { params })
}

export function getSkillById(id) {
  return request.get('/skills/' + id)
}

export function getSkillByUser(userId) {
  return request.get('/skills/user/' + userId)
}

export function createSkill(data) {
  return request.post('/skills', data)
}

export function updateSkill(data) {
  return request.put('/skills', data)
}

export function deleteSkill(id) {
  return request.delete('/skills/' + id)
}

export function likeSkill(id) {
  return request.post('/skills/' + id + '/like')
}

export function collectSkill(id) {
  return request.post('/skills/' + id + '/collect')
}