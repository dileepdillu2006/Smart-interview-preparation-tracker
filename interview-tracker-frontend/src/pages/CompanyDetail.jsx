import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/api';
import './CompanyDetail.css';

const STAGES = ['APPLIED', 'OA_SCHEDULED', 'OA_COMPLETED', 'INTERVIEW_ROUND_1', 'INTERVIEW_ROUND_2', 'INTERVIEW_ROUND_3', 'HR_ROUND', 'OFFER', 'REJECTED'];

function CompanyDetail() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [company, setCompany] = useState(null);
  const [currentStage, setCurrentStage] = useState(null);
  const [problems, setProblems] = useState([]);
  const [questions, setQuestions] = useState('');
  const [genLoading, setGenLoading] = useState(false);

  const [showProblemForm, setShowProblemForm] = useState(false);
  const [problemForm, setProblemForm] = useState({ title: '', difficulty: 'EASY', topic: '', status: 'SOLVED', datePracticed: '' });
  const [editingId, setEditingId] = useState(null);

  const [chatMessages, setChatMessages] = useState([]);
  const [chatInput, setChatInput] = useState('');
  const [chatLoading, setChatLoading] = useState(false);
  const [interviewStarted, setInterviewStarted] = useState(false);

  useEffect(() => {
    setChatMessages([]);
    setInterviewStarted(false);
    loadData();
  }, [id]);

  const loadData = async () => {
    try {
      const c = await api.getCompany(id);
      setCompany(c);
      setQuestions(c.generatedQuestions || '');
      const stage = await api.getCurrentStage(id);
      setCurrentStage(stage);
      const probs = await api.getProblemsByCompany(id);
      setProblems(probs);
    } catch (err) {
      console.error(err);
    }
  };

  const handleAdvanceStage = async (newStage) => {
    try {
      await api.advanceStage(id, newStage, '');
      loadData();
    } catch (err) {
      alert('Failed to update stage');
    }
  };

  const handleEditProblem = (p) => {
    setEditingId(p.id);
    setProblemForm({ title: p.title, difficulty: p.difficulty, topic: p.topic || '', status: p.status, datePracticed: p.datePracticed || '' });
    setShowProblemForm(true);
  };

  const handleUpdateProblem = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.updateProblem(editingId, problemForm);
      } else {
        await api.createProblem(user.userId, id, problemForm);
      }
      setProblemForm({ title: '', difficulty: 'EASY', topic: '', status: 'SOLVED', datePracticed: '' });
      setEditingId(null);
      setShowProblemForm(false);
      loadData();
    } catch (err) {
      alert('Failed to save problem');
    }
  };

  const handleDeleteProblem = async (problemId) => {
    if (!confirm('Delete this problem?')) return;
    try {
      await api.deleteProblem(problemId);
      loadData();
    } catch (err) {
      alert('Failed to delete problem');
    }
  };

  const handleCancelProblemForm = () => {
    setShowProblemForm(false);
    setEditingId(null);
    setProblemForm({ title: '', difficulty: 'EASY', topic: '', status: 'SOLVED', datePracticed: '' });
  };

  const handleGenerateQuestions = async () => {
    setGenLoading(true);
    try {
      const res = await api.generateQuestions(id);
      setQuestions(res.reply);
    } catch (err) {
      alert('Failed to generate questions');
    } finally {
      setGenLoading(false);
    }
  };

  const handleStartInterview = async () => {
    setChatLoading(true);
    setInterviewStarted(true);
    try {
      const res = await api.startInterview(id, user.userId);
      setChatMessages([{ role: 'interviewer', content: res.reply }]);
    } catch (err) {
      alert('Failed to start interview');
    } finally {
      setChatLoading(false);
    }
  };

  const handleSendAnswer = async (e) => {
    e.preventDefault();
    if (!chatInput.trim()) return;

    const newHistory = [...chatMessages, { role: 'candidate', content: chatInput }];
    setChatMessages(newHistory);
    setChatInput('');
    setChatLoading(true);

    try {
      const res = await api.nextQuestion({ companyId: Number(id), history: newHistory });
      setChatMessages([...newHistory, { role: 'interviewer', content: res.reply }]);
    } catch (err) {
      alert('Failed to get next question');
    } finally {
      setChatLoading(false);
    }
  };

  if (!company) return <div className="app-container"><p className="loading-text">Loading...</p></div>;

  return (
    <div className="app-container">
      <button className="back-btn" onClick={() => navigate('/dashboard')}>← Back</button>

      <div className="detail-header">
        <h1>{company.name}</h1>
        <p className="detail-role">{company.role}</p>
        {currentStage && (
          <span className="stage-badge">{currentStage.stage.replace(/_/g, ' ')}</span>
        )}
      </div>

      <div className="detail-section">
        <h2>Update Stage</h2>
        <div className="stage-buttons">
          {STAGES.map((stage) => (
            <button
              key={stage}
              className={`stage-btn ${currentStage?.stage === stage ? 'active' : ''}`}
              onClick={() => handleAdvanceStage(stage)}
            >
              {stage.replace(/_/g, ' ')}
            </button>
          ))}
        </div>
      </div>

      <div className="detail-section">
        <h2>Progress Overview</h2>
        <div className="progress-stats">
          <div className="stat-box">
            <span className="stat-number">{problems.length}</span>
            <span className="stat-label">Problems Logged</span>
          </div>
          <div className="stat-box">
            <span className="stat-number">{problems.filter(p => p.status === 'SOLVED').length}</span>
            <span className="stat-label">Solved</span>
          </div>
          <div className="stat-box">
            <span className="stat-number">{STAGES.indexOf(currentStage?.stage) + 1}/{STAGES.length}</span>
            <span className="stat-label">Stage Progress</span>
          </div>
        </div>
      </div>

      <div className="detail-section">
        <h2>Problems Practiced ({problems.length})</h2>
        <button className="ai-btn small-btn" onClick={() => (showProblemForm ? handleCancelProblemForm() : setShowProblemForm(true))}>
          {showProblemForm ? 'Cancel' : '+ Add Problem'}
        </button>

        {showProblemForm && (
          <form className="inline-form" onSubmit={handleUpdateProblem}>
            <input
              placeholder="Problem title (e.g. Two Sum)"
              value={problemForm.title}
              onChange={(e) => setProblemForm({ ...problemForm, title: e.target.value })}
              required
            />
            <select
              value={problemForm.difficulty}
              onChange={(e) => setProblemForm({ ...problemForm, difficulty: e.target.value })}
            >
              <option value="EASY">Easy</option>
              <option value="MEDIUM">Medium</option>
              <option value="HARD">Hard</option>
            </select>
            <input
              placeholder="Topic (e.g. Arrays)"
              value={problemForm.topic}
              onChange={(e) => setProblemForm({ ...problemForm, topic: e.target.value })}
            />
            <select
              value={problemForm.status}
              onChange={(e) => setProblemForm({ ...problemForm, status: e.target.value })}
            >
              <option value="SOLVED">Solved</option>
              <option value="UNSOLVED">Unsolved</option>
              <option value="REVISIT">Revisit</option>
            </select>
            <input
              type="date"
              value={problemForm.datePracticed}
              onChange={(e) => setProblemForm({ ...problemForm, datePracticed: e.target.value })}
            />
            <button type="submit">{editingId ? 'Update Problem' : 'Save Problem'}</button>
          </form>
        )}

        {problems.length === 0 ? (
          <p className="empty-text-small">No problems logged for this company yet.</p>
        ) : (
          <ul className="problem-list">
            {problems.map((p) => (
              <li key={p.id} className="problem-item">
                <span>{p.title}</span>
                <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                  <span className={`difficulty-badge difficulty-${p.difficulty.toLowerCase()}`}>{p.difficulty}</span>
                  <button className="mini-btn" onClick={() => handleEditProblem(p)}>Edit</button>
                  <button className="mini-btn danger" onClick={() => handleDeleteProblem(p.id)}>Delete</button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

      <div className="detail-section">
        <h2>AI-Generated Practice Questions</h2>
        <button className="ai-btn" onClick={handleGenerateQuestions} disabled={genLoading}>
          {genLoading ? 'Generating...' : 'Generate Questions from JD'}
        </button>
        {questions && <pre className="questions-output">{questions}</pre>}
      </div>

      <div className="detail-section">
        <h2>AI Mock Interview</h2>
        {!interviewStarted ? (
          <button className="ai-btn" onClick={handleStartInterview}>
            Start Mock Interview
          </button>
        ) : (
          <div className="chat-box">
            <div className="chat-messages">
              {chatMessages.map((msg, i) => (
                <div key={i} className={`chat-msg ${msg.role}`}>
                  <span className="chat-role">{msg.role === 'interviewer' ? 'Interviewer' : 'You'}</span>
                  <p>{msg.content}</p>
                </div>
              ))}
              {chatLoading && <div className="chat-msg interviewer"><p>Thinking...</p></div>}
            </div>
            <form className="chat-input-form" onSubmit={handleSendAnswer}>
              <input
                value={chatInput}
                onChange={(e) => setChatInput(e.target.value)}
                placeholder="Type your answer..."
                disabled={chatLoading}
              />
              <button type="submit" disabled={chatLoading}>Send</button>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}

export default CompanyDetail;