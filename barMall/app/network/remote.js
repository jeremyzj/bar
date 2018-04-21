import queryString from 'query-string'
export const api = {
  schemes: 'http',
  host: '120.77.216.85',
  basePath: '',
  port: '',
  domain: 'http://120.77.216.85/dbshop'
}

const defaults = {
  method: 'post',
  credentials: 'include',
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
  }
}

const checkStatus = response => {
  if (response.status >= 200 && response.status < 300) {
    return response
  } else if (response.status === 503) {
    return Promise.reject(new Error(JSON.stringify({
      code: 503,
      msg: '系统维护中，请稍后再试'
    })))
  } else {
    var error = new Error(response.statusText)
    error.detail = response

    return Promise.reject(error)
  }
}

const parseJSON = response => {
  if (__DEV__) {
    console.log('返回', response)
  }
  return response.json()
}

const errorHandler = error => {
  return Promise.reject(error)
}

const request = (path, options) => {
  let opt = { ...defaults, ...options }
  let url = api.domain + path

  if (opt.body && typeof opt.body === 'object') {
    opt.body = queryString.stringify(opt.body)
  }

  if (opt.method === 'GET' && typeof opt.body !== 'undefined') {
    url += '?' + opt.body
    delete opt.body
  }

  if (__DEV__) {
    console.log('请求', url , opt)
  }

  return fetch(url, opt).then(checkStatus).then(parseJSON).catch(errorHandler)
}

export default request

export const get = (path, options) => {
  return request(path, { ...options, ...{ method: 'GET' } })
}

export const post = (path, options) => {
  return request(path, { ...options, ...{ method: 'POST' } })
}