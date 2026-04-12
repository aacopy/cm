import { useState, useCallback, useRef } from 'react'
import { sendEmailCode, loginByEmailCode } from '../../api/auth'
import './LoginPage.css'

const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
const CODE_LENGTH = 6
const AUTH_GROUP_ID = Number(import.meta.env.VITE_AUTH_GROUP_ID) || 1

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [code, setCode] = useState('')
  const [emailError, setEmailError] = useState('')
  const [codeError, setCodeError] = useState('')
  const [countdown, setCountdown] = useState(0)
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null)
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null)

  const startCountdown = useCallback(() => {
    setCountdown(60)
    timerRef.current = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          clearInterval(timerRef.current!)
          return 0
        }
        return prev - 1
      })
    }, 1000)
  }, [])

  const validateEmail = (value: string): boolean => {
    if (!value) {
      setEmailError('请输入邮箱')
      return false
    }
    if (!EMAIL_REGEX.test(value)) {
      setEmailError('邮箱格式不正确')
      return false
    }
    setEmailError('')
    return true
  }

  const handleSendCode = async () => {
    if (!validateEmail(email)) return
    if (countdown > 0) return

    try {
      await sendEmailCode({ authGroupId: AUTH_GROUP_ID, email })
      setMessage({ type: 'success', text: '验证码已发送' })
      startCountdown()
    } catch (err) {
      setMessage({ type: 'error', text: (err as Error).message })
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    const isEmailValid = validateEmail(email)
    if (!isEmailValid) return

    if (!code) {
      setCodeError('请输入验证码')
      return
    }
    if (code.length !== CODE_LENGTH) {
      setCodeError('验证码为6位数字')
      return
    }
    setCodeError('')
    setLoading(true)
    setMessage(null)

    try {
      const { data } = await loginByEmailCode({
        authGroupId: AUTH_GROUP_ID,
        email,
        code,
      })
      setMessage({ type: 'success', text: '登录成功' })
      // Store token for future requests
      if (data.data?.token) {
        localStorage.setItem('token', data.data.token)
      }
    } catch (err) {
      setMessage({ type: 'error', text: (err as Error).message })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <div className="login-card">
        {/* Left panel - Branding */}
        <div className="login-brand">
          <h1 className="brand-title">CM Auth</h1>
        </div>

        {/* Right panel - Form */}
        <div className="login-form-panel">
          <h2 className="form-title">欢迎回来</h2>
          <p className="form-subtitle">使用邮箱验证码登录</p>

          {message && (
            <div className={`message ${message.type}`}>{message.text}</div>
          )}

          <form onSubmit={handleSubmit} className="form-content">
            {/* Email field */}
            <div className="form-field">
              <label className="field-label" htmlFor="email">
                邮箱地址
              </label>
              <div className="field-input-wrapper">
                <svg className="field-icon" viewBox="0 0 24 20" fill="none" stroke="#9CA3AF" strokeWidth="2">
                  <path d="M2 4l6 5 6-5" />
                  <rect x="1" y="3" width="22" height="14" rx="1" />
                </svg>
                <input
                  id="email"
                  className={`field-input ${emailError ? 'error' : ''}`}
                  type="email"
                  placeholder="请输入邮箱"
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value)
                    if (emailError) validateEmail(e.target.value)
                  }}
                />
              </div>
              {emailError && <span className="field-error">{emailError}</span>}
            </div>

            {/* Verification code field */}
            <div className="form-field">
              <label className="field-label" htmlFor="code">
                验证码
              </label>
              <div className="code-row">
                <div className="field-input-wrapper code-input-wrapper">
                  <svg className="field-icon" viewBox="0 0 16 16" fill="none" stroke="#9CA3AF" strokeWidth="1.5">
                    <rect x="3" y="5" width="10" height="8" rx="1" />
                    <path d="M3 5h10V4a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v1z" />
                  </svg>
                  <input
                    id="code"
                    className={`field-input ${codeError ? 'error' : ''}`}
                    type="text"
                    placeholder="请输入6位验证码"
                    maxLength={CODE_LENGTH}
                    value={code}
                    onChange={(e) => {
                      const val = e.target.value.replace(/\D/g, '')
                      setCode(val)
                      if (codeError && val.length === CODE_LENGTH) setCodeError('')
                    }}
                  />
                </div>
                <button
                  type="button"
                  className="send-code-btn"
                  disabled={countdown > 0}
                  onClick={handleSendCode}
                >
                  {countdown > 0 ? `${countdown}s` : '获取验证码'}
                </button>
              </div>
              {codeError && <span className="field-error">{codeError}</span>}
            </div>

            {/* Submit button */}
            <button type="submit" className="submit-btn" disabled={loading}>
              {loading ? '登录中...' : '登 录'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
