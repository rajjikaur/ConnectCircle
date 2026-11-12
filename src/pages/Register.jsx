
import { useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'

const API_BASE_URL = 'http://localhost:8080/api'

function Register() {
  const { role } = useParams()
  const navigate = useNavigate()

  const roleNames = {
    brand: 'Brand',
    influencer: 'Creator',
    consumer: 'Consumer',
  }

 const backendRoles = {
  brand: 'BRAND',
  influencer: 'INFLUENCER',
  consumer: 'CONSUMER',
}

  const roleName = roleNames[role]

  const [formData, setFormData] = useState({
    fullName: '',
    phone: '',
    email: '',
    password: '',
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    })
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          fullName: formData.fullName.trim(),
          phone: formData.phone.trim(),
          email: formData.email.trim(),
          password: formData.password,
          role: backendRoles[role],
        }),
      })

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || 'Registration failed.')
      }

      alert('Account created successfully! Please log in.')
      navigate('/login')
    } catch (err) {
      setError(err.message || 'Unable to connect to the backend.')
    } finally {
      setLoading(false)
    }
  }

  if (!roleName) {
    return (
      <main className="role-page">
        <h1>Role not found</h1>
        <Link to="/get-started">Choose your role</Link>
      </main>
    )
  }

  return (
    <main className="role-page">
      <Link to="/get-started" className="back-link">
        ← Change role
      </Link>

      <section className="register-card">
        <Link to="/" className="logo register-logo">
          <span className="logo-icon">C</span>
          ConnectCircle
        </Link>

        <span className="eyebrow">CREATE YOUR ACCOUNT</span>
        <h1>Join as a {roleName}</h1>
        <p>Start building meaningful connections.</p>

        <form onSubmit={handleSubmit}>
          <label htmlFor="name">Full name</label>
          <input
            id="name"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            placeholder="Enter your name"
            required
          />

          <label htmlFor="phone">Phone number</label>
          <input
            id="phone"
            name="phone"
            type="tel"
            value={formData.phone}
            onChange={handleChange}
            placeholder="Enter your phone number"
            minLength={10}
            maxLength={20}
            required
          />

          <label htmlFor="email">Email address</label>
          <input
            id="email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="you@example.com"
            required
          />

          <label htmlFor="password">Password</label>
          <input
            id="password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Create a password"
            minLength={8}
            required
          />

          {error && (
            <p role="alert" style={{ color: 'red' }}>
              {error}
            </p>
          )}

          <button
            type="submit"
            className="primary-btn register-submit"
            disabled={loading}
          >
            {loading ? 'Creating account...' : 'Create account →'}
          </button>
        </form>

        <p className="register-footer">
          Already registered? <Link to="/login">Log in</Link>
        </p>
      </section>
    </main>
  )
}

export default Register
