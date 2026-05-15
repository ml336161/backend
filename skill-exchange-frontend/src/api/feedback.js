import request from './axios'

export function createFeedback(data) {
  return request.post('/feedback', data)
}

export function getMyFeedbacks() {
  return request.get('/feedback/my')
}

export function getAllFeedbacks() {
  return request.get('/feedback/all')
}