// main.jsx is the entry point of your React application. It loads App.jsx into the HTML page.
// Entry point that renders the React App into the browser DOM.
// Render means display or show something on the broswer screen.

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
