import request from './axios'

export function applyFriend(data) {
  return request.post('/friends/apply', data)
}

export function handleApply(data) {
  return request.put('/friends/handle', data)
}

export function getReceivedApplies() {
  return request.get('/friends/received')
}

export function getSentApplies() {
  return request.get('/friends/sent')
}

export function getFriends() {
  return request.get('/friends')
}

export function deleteFriend(friendUserId) {
  return request.delete(`/friends/${friendUserId}`)
}