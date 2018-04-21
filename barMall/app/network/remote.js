import queryString from 'query-string'
export const api = {
  schemes: 'http',
  host: 'hafuyun.com',
  basePath: '',
  port: '',
  domain: 'http://hafuyun.com'
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

const parseJSON = (method, url, opt) => {
  return response => {
    return response.text().then(text => {
      const json = JSON.parse(text)

      if (__DEV__) {
        console.log(method + '：', url, (opt.body ? opt.body : ''))
        console.log('返回：', json)
      }

      return json
    })
  }
}

const checkAuth = response => {
  if (!response.success) {
    return Promise.reject(new Error(response.msg))
  }

  return response
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

  return fetch(url, opt).then(checkStatus).then(parseJSON(opt.method, url, opt)).then(checkAuth).catch(errorHandler)
}

export default request

export const get = (path, options) => {
  return request(path, { ...options, ...{ method: 'GET' } })
}

export const post = (path, options) => {
  return request(path, { ...options, ...{ method: 'POST' } })
}