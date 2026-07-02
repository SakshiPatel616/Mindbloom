/* ============================================================
   MindBloom – API Client
   All calls to Spring Boot at /api
   ============================================================ */

const API_BASE = 'http://localhost:8080/api';

const api = {
  /* ---------- Core ---------- */
  _token() {
    return localStorage.getItem('mb_token');
  },

 
  
  async _req(method, path, body = null) {
    const headers = { 'Content-Type': 'application/json' };

    const token = this._token();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const opts = { method, headers };

    if (body) {
      opts.body = JSON.stringify(body);
    }

    const res = await fetch(`${API_BASE}${path}`, opts);

    const text = await res.text();
    const data = text ? JSON.parse(text) : {};

    if (!res.ok) {
      throw new Error(data.message || `Request failed (${res.status})`);
    }

    return data.data ?? data;
  },

  get:    (path)        => api._req('GET',    path),
  post:   (path, body)  => api._req('POST',   path, body),
  put:    (path, body)  => api._req('PUT',    path, body),
  delete: (path)        => api._req('DELETE', path),
  patch:  (path, body)  => api._req('PATCH',  path, body),

  /* ---------- Auth ---------- */
  auth: {
    register: (data) => api.post('/auth/register', data),
    login:    (data) => api.post('/auth/login',    data),
  },

  /* ---------- Onboarding ---------- */
  onboarding: {
    complete: (data) => api.post('/onboarding', data),
  },

  /* ---------- Mood ---------- */
  mood: {
    log:     (data) => api.post('/mood', data),
    summary: ()     => api.get('/mood/summary'),
  },

  /* ---------- Journal ---------- */
  journal: {
    create:  (data)     => api.post('/journal', data),
    list:    (page = 0) => api.get(`/journal?page=${page}&size=10`),
    get:     (id)       => api.get(`/journal/${id}`),
    delete:  (id)       => api.delete(`/journal/${id}`),
  },

  /* ---------- Chat ---------- */
  chat: {
    send:    (data)      => api.post('/chat', data),
    history: (sessionId) => api.get(`/chat/history/${sessionId}`),
  },

  /* ---------- Exercises ---------- */
  exercises: {
    all:         ()     => api.get('/exercises'),
    byType:      (type) => api.get(`/exercises/type/${type}`),
    recommended: ()     => api.get('/exercises/recommended'),
  },
};
