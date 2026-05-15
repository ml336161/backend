import request from './axios'

export function createExchange(data) {
  return request.post('/exchanges', data)
}

export function handleExchange(data) {
  return request.put('/exchanges/handle', data)
}

export function completeExchange(id) {
  return request.put('/exchanges/' + id + '/complete')
}

export function getReceivedExchanges() {
  return request.get('/exchanges/received')
}

export function getSentExchanges() {
  return request.get('/exchanges/sent')
}

export function getPendingCount() {
  return request.get('/exchanges/pending-count')
}