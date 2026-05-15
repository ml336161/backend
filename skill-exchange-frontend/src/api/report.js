import request from './axios'

export function createReport(data) {
  return request.post('/reports', data)
}

export function getMyReports() {
  return request.get('/reports/my')
}