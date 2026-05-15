import request from './axios'

export function getDashboardStats() {
  return request.get('/admin/dashboard')
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/user/${id}/status`, null, { params: { status } })
}

export function deleteSkill(id) {
  return request.delete(`/admin/skill/${id}`)
}

export function handleReport(data) {
  return request.put('/admin/report/handle', data)
}

export function replyFeedback(data) {
  return request.put('/admin/feedback/reply', data)
}

export function getReports(status) {
  if (status) {
    return request.get('/admin/reports', { params: { status } })
  }
  return request.get('/admin/reports')
}

export function getFeedbacks() {
  return request.get('/admin/feedbacks')
}