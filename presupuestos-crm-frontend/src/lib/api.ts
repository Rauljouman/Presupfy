
import axios from 'axios'
import { supabase } from './supabase'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000,
})

api.interceptors.request.use(async (config) => {
  const { data } = await supabase.auth.getSession()

  if (data.session?.access_token) {
    config.headers.set(
      'Authorization',
      `Bearer ${data.session.access_token}`
    )
  }

  return config
})
