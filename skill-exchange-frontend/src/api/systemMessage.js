import request from './axios'

export function getSystemMessages() {
  return request.get('/system-message')
}

export function getUnreadCount() {
  return request.get('/system-message/unread-count')
}

export function markAllRead() {
  return request.post('/system-message/mark-read')
}
