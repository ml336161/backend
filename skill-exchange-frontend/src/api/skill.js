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

export function getHotSkills(params) {
  return request.get('/skills/hot', { params })
}

export function getLatestSkills(params) {
  return request.get('/skills/latest', { params })
}

export function getUserCollects() {
  return request.get('/skills/collects')
}

export function getUserLikes() {
  return request.get('/skills/likes')
}

export function getUserLikesByUserId(userId) {
  return request.get('/skills/user/' + userId + '/likes')
}

export function getUserCollectsByUserId(userId) {
  return request.get('/skills/user/' + userId + '/collects')
}

export function getCollectCount() {
  return request.get('/skills/collects/count')
}

export function getLikeCount() {
  return request.get('/skills/likes/count')
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