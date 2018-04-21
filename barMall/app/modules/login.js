import Immutable from 'immutable'
import { get, post } from '../network/remote'

export const types = {
  CODE: 'login.CODE',   //获取验证码
  LOGIN: 'login.LOGIN', //登录
}

const sendCodeAPI =  '/front/user/user/sendCode'
const loginAPI = '/front/user/user/checkCode'

export const getCodeStatus = {
  GETCODE_STATUS_IDEL: 'GETCODE_STATUS_IDEL',
  GETCODE_STATUS_ING: 'GETCODE_STATUS_ING',
  GETCODE_STATUS_DONE: 'GETCODE_STATUS_DONE',
  GETCODE_STATUS_FAIL: 'GETCODE_STATUS_FAIL'
}

export const actions = {
  [types.CODE]: (params) => dispatch => {
    let body = {...params}
    return get(sendCodeAPI, { body }).then(response => {
      dispatch({type: types.CODE, payload:response})
      return Promise.resolve(response)
    })
  },
  [types.LOGIN]: (params) => dispatch => {
    return post(loginAPI, { ...params }).then(response => {
      dispatch({type: types.LOGIN, payload: response})
      return Promise.resolve(response)
    })
  },
}

const initialState = Immutable.fromJS({
  [types.CODE]: '',
  [types.LOGIN]: ''
})

export default (state = initialState, action) => {
  switch (action.type) {
    case types.LOGIN:
    case types.CODE:
      return state.set(action.type, action.payload)
    default:
      return state
  }
} 
