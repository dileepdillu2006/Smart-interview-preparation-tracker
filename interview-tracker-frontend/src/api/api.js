const BASE_URL = 'http://localhost:8080/api';

function getToken() {
  return localStorage.getItem('token');
}

async function request(endpoint, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Request failed: ${response.status}`);
  }

  // Handle empty responses (like DELETE calls)
  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

export const api = {
  register: (data) => request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
  login: (data) => request('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
updateUser: (id, data) => request(`/users/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  getUser: (id) => request(`/users/${id}`),
  updateProblem: (id, data) => request(`/problems/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteProblem: (id) => request(`/problems/${id}`, { method: 'DELETE' }),
  getCompanies: (userId) => request(`/companies/user/${userId}`),
  getCompany: (id) => request(`/companies/${id}`),
  createCompany: (userId, data) => request(`/companies/user/${userId}`, { method: 'POST', body: JSON.stringify(data) }),
  updateCompany: (id, data) => request(`/companies/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteCompany: (id) => request(`/companies/${id}`, { method: 'DELETE' }),
  getCurrentStage: (companyId) => request(`/companies/${companyId}/current-stage`),
  advanceStage: (companyId, stage, notes) =>
    request(`/companies/${companyId}/advance-stage?stage=${stage}&notes=${encodeURIComponent(notes || '')}`, { method: 'POST' }),

  getProblemsByCompany: (companyId) => request(`/problems/company/${companyId}`),
  createProblem: (userId, companyId, data) =>
    request(`/problems/user/${userId}${companyId ? `?companyId=${companyId}` : ''}`, { method: 'POST', body: JSON.stringify(data) }),

  getMockInterviewsByCompany: (companyId) => request(`/mock-interviews/company/${companyId}`),

  startInterview: (companyId, userId) => request(`/ai/interview/start/${companyId}/user/${userId}`, { method: 'POST' }),
  nextQuestion: (data) => request('/ai/interview/next', { method: 'POST', body: JSON.stringify(data) }),
  generateQuestions: (companyId) => request(`/ai/questions/${companyId}`, { method: 'POST' }),
};