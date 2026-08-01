import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/api';
import './Dashboard.css';
import './CompanyDetail.css';

function Profile() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [resumeText, setResumeText] = useState('');
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    api.getUser(user.userId).then((u) => setResumeText(u.resumeText || ''));
  }, []);

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setSaved(false);
    try {
      await api.updateUser(user.userId, { name: user.name, email: user.email, password: '', resumeText });
      setSaved(true);
    } catch (err) {
      alert('Failed to save resume');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="app-container">
      <button className="back-btn" onClick={() => navigate('/dashboard')}>← Back</button>

      <div className="detail-header">
        <h1>{user.name}</h1>
        <p className="detail-role">{user.email}</p>
      </div>

      <div className="detail-section">
        <h2>Resume</h2>
        <p className="empty-text-small" style={{ marginBottom: '12px' }}>
          Paste your resume text here — it's used to personalize your AI mock interview questions.
        </p>
        <form onSubmit={handleSave}>
          <textarea
            className="resume-textarea"
            value={resumeText}
            onChange={(e) => setResumeText(e.target.value)}
            rows={14}
            placeholder="Paste your resume content here..."
          />
          <button type="submit" className="ai-btn" disabled={saving} style={{ marginTop: '12px' }}>
            {saving ? 'Saving...' : 'Save Resume'}
          </button>
          {saved && <span style={{ color: '#86efac', marginLeft: '12px', fontSize: '0.85rem' }}>Saved!</span>}
        </form>
      </div>
    </div>
  );
}

export default Profile;