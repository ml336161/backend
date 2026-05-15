import request from './axios'

export function getCommentsBySkill(skillId) {
  return request.get('/comments/skill/' + skillId)
}

export function createComment(data) {
  return request.post('/comments', data)
}

export function deleteComment(id) {
  return request.delete('/comments/' + id)
}