import request from './axios'

export function sendMessage(data) {
  return request.post('/chat/send', data)
}

export function getConversation(otherUserId) {
  return request.get('/chat/conversation/' + otherUserId)
}

export function getUnreadCount() {
  return request.get('/chat/unread-count')
}

export function markAsRead(fromUserId) {
  return request.put('/chat/mark-read/' + fromUserId)
}