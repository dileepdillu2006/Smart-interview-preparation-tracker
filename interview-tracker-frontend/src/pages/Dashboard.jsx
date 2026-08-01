import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/api';
import './Dashboard.css';

function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', role: '', priority: 'MEDIUM', applicationDeadline: '', jobDescription: '', notes: '' });

  useEffect(() => {
    loadCompanies();
  }, []);

  const loadCompanies = async () => {
    try {
      const data = await api.getCompanies(user.userId);
      setCompanies(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddCompany = async (e) => {
    e.preventDefault();
    try {
      await api.createCompany(user.userId, form);
      setForm({ name: '', role: '', priority: 'MEDIUM', applicationDeadline: '', jobDescription: '', notes: '' });
      setShowForm(false);
      loadCompanies();
    } catch (err) {
      alert('Failed to add company');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="app-container">
     <div className="dashboard-header">
        <div>
          <h1 className="app-title">Placement Prep Tracker</h1>
          <p className="app-subtitle">Welcome back, {user.name}</p>
        </div>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button className="logout-btn" onClick={() => navigate('/profile')}>Profile</button>
          <button className="logout-btn" onClick={handleLogout}>Log Out</button>
        </div>
      </div>
      <button className="add-company-btn" onClick={() => setShowForm(!showForm)}>
        {showForm ? 'Cancel' : '+ Add Company'}
      </button>

      {showForm && (
        <form className="company-form" onSubmit={handleAddCompany}>
          <input
            placeholder="Company name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <input
            placeholder="Role (e.g. SDE Intern)"
            value={form.role}
            onChange={(e) => setForm({ ...form, role: e.target.value })}
            required
          />
          <select
            value={form.priority}
            onChange={(e) => setForm({ ...form, priority: e.target.value })}
          >
            <option value="HIGH">High Priority</option>
            <option value="MEDIUM">Medium Priority</option>
            <option value="LOW">Low Priority</option>
          </select>
          <input
            type="date"
            value={form.applicationDeadline}
            onChange={(e) => setForm({ ...form, applicationDeadline: e.target.value })}
          />
          <textarea
            placeholder="Job description (paste here for AI-generated questions)"
            value={form.jobDescription}
            onChange={(e) => setForm({ ...form, jobDescription: e.target.value })}
            rows={4}
          />
          <input
            placeholder="Notes (optional)"
            value={form.notes}
            onChange={(e) => setForm({ ...form, notes: e.target.value })}
          />
          <button type="submit">Save Company</button>
        </form>
      )}

      {loading ? (
        <p className="loading-text">Loading...</p>
      ) : companies.length === 0 ? (
        <p className="empty-text">No companies yet. Add your first one above.</p>
      ) : (
        <ul className="company-list">
          {companies.map((company) => (
            <li
              key={company.id}
              className="company-card"
              onClick={() => navigate(`/company/${company.id}`)}
            >
              <div className="company-info">
                <h3>{company.name}</h3>
                <p>{company.role}</p>
              </div>
              <span className={`priority-badge priority-${company.priority.toLowerCase()}`}>
                {company.priority}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Dashboard;