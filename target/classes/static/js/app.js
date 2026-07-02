/* ============================================================
   MindBloom – App Utilities
   ============================================================ */

/* ---------- Auth state ---------- */
const auth = {
  save(token, user) {
    localStorage.setItem('mb_token', token);
    localStorage.setItem('mb_user', JSON.stringify(user));
  },
  clear() {
    localStorage.removeItem('mb_token');
    localStorage.removeItem('mb_user');
  },
  token() { return localStorage.getItem('mb_token'); },
  user()  {
    try { return JSON.parse(localStorage.getItem('mb_user')); }
    catch { return null; }
  },
  isLoggedIn() { return !!this.token(); },
  requireAuth() {
    if (!this.isLoggedIn()) {
      window.location.href = '/api/pages/login.html';
      return false;
    }
    return true;
  },
  
  requireOnboarding() {
    const u = this.user();
    if (u && !u.onboardingCompleted) {
     
	  window.location.href = '/api/pages/onboarding.html';
      return false;
    }
    return true;
  },
  logout() {
    this.clear();
    window.location.href = '/api/pages/login.html';
	
  }
};

/* ---------- Toast ---------- */
const toast = (() => {
  let container;
  function getContainer() {
    if (!container) {
      container = document.createElement('div');
      container.className = 'toast-container';
      document.body.appendChild(container);
    }
    return container;
  }
  function show(message, type = 'default', duration = 3500) {
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    const icons = { success: '✓', error: '✕', warning: '⚠' };
    t.innerHTML = `<span>${icons[type] || 'ℹ'}</span><span>${message}</span>`;
    getContainer().appendChild(t);
    setTimeout(() => {
      t.style.opacity = '0';
      t.style.transform = 'translateY(10px)';
      t.style.transition = 'all 0.25s ease';
      setTimeout(() => t.remove(), 250);
    }, duration);
  }
  return {
    success: (m) => show(m, 'success'),
    error:   (m) => show(m, 'error'),
    warning: (m) => show(m, 'warning'),
    info:    (m) => show(m, 'default'),
  };
})();

/* ---------- Helpers ---------- */
function $(sel, ctx = document) { return ctx.querySelector(sel); }
function $$(sel, ctx = document) { return [...ctx.querySelectorAll(sel)]; }

function formatDate(dateStr) {
  const d = new Date(dateStr);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}
function formatTime(dateStr) {
  return new Date(dateStr).toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
}
function timeAgo(dateStr) {
  const diff = Date.now() - new Date(dateStr).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1)  return 'just now';
  if (m < 60) return `${m}m ago`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h}h ago`;
  return formatDate(dateStr);
}
function truncate(str, n = 120) {
  return str.length > n ? str.slice(0, n) + '…' : str;
}
function setLoading(btn, loading, text = 'Save') {
  btn.disabled = loading;
  btn.innerHTML = loading
    ? `<span class="spinner"></span>`
    : text;
}

/* ---------- Session ID for chat ---------- */
function getOrCreateSession() {
  let sid = sessionStorage.getItem('mb_chat_session');
  if (!sid) {
    sid = crypto.randomUUID();
    sessionStorage.setItem('mb_chat_session', sid);
  }
  return sid;
}
function newSession() {
  const sid = crypto.randomUUID();
  sessionStorage.setItem('mb_chat_session', sid);
  return sid;
}

/* ---------- Inject sidebar ---------- */
function initSidebar() {
  const user = auth.user();
  if (!user) return;

  const page = document.body.dataset.page || '';
  const initials = user.displayName
    ? user.displayName.split(' ').map(w => w[0]).join('').slice(0,2).toUpperCase()
    : '?';

  const sidebar = document.getElementById('sidebar');
  if (!sidebar) return;

  sidebar.innerHTML = `
    <div class="sidebar-logo">
      <div class="logo-mark">🌱</div>
      <span>MindBloom</span>
    </div>

    <div class="sidebar-user">
      <div class="user-avatar">${initials}</div>
      <div class="user-info">
        <p>${user.displayName}</p>
        <span>${user.healingPath ? user.healingPath.replace('_',' ') : 'Wellness path'}</span>
      </div>
    </div>

    <span class="nav-section-label">Main</span>
    <a href="/api/pages/dashboard.html" class="nav-link ${page==='dashboard'?'active':''}">
      ${iconHome()} Dashboard
    </a>
    <a href="/api/pages/chat.html" class="nav-link ${page==='chat'?'active':''}">
      ${iconChat()} AI Companion
    </a>

    <span class="nav-section-label">Track</span>
    <a href="/api/pages/mood.html" class="nav-link ${page==='mood'?'active':''}">
      ${iconMood()} Mood Tracker
    </a>
    <a href="/api/pages/journal.html" class="nav-link ${page==='journal'?'active':''}">
      ${iconJournal()} Journal
    </a>

    <span class="nav-section-label">Heal</span>
    <a href="/api/pages/exercises.html" class="nav-link ${page==='exercises'?'active':''}">
      ${iconExercise()} Exercises
    </a>

    <div class="sidebar-footer">
      <p>🔒 Your data is private. We are a companion, not a replacement for professional care.</p>
      <button onclick="auth.logout()" style="background:none;border:none;color:rgba(255,255,255,0.4);font-size:0.75rem;margin-top:0.5rem;cursor:pointer;padding:0">Sign out</button>
    </div>
  `;
}

/* ---------- SVG Icons ---------- */
const iconHome     = () => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>`;
const iconChat     = () => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>`;
const iconMood     = () => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M8 14s1.5 2 4 2 4-2 4-2"/><line x1="9" y1="9" x2="9.01" y2="9"/><line x1="15" y1="9" x2="15.01" y2="9"/></svg>`;
const iconJournal  = () => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 014 4v14a3 3 0 00-3-3H2z"/><path d="M22 3h-6a4 4 0 00-4 4v14a3 3 0 013-3h7z"/></svg>`;
const iconExercise = () => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8h1a4 4 0 010 8h-1"/><path d="M2 8h16v9a4 4 0 01-4 4H6a4 4 0 01-4-4V8z"/><line x1="6" y1="1" x2="6" y2="4"/><line x1="10" y1="1" x2="10" y2="4"/><line x1="14" y1="1" x2="14" y2="4"/></svg>`;
