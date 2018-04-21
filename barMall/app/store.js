import { createStore, applyMiddleware } from 'redux'
import Immutable from 'immutable'
import { combineReducers } from 'redux-immutablejs'
import thunk from 'redux-thunk'
import { composeWithDevTools } from 'redux-devtools-extension'
// import loginMiddleware from './middlewares/loginMiddleware'
import reducers from './reducers'
// import screenTracking from './middlewares/trackingMiddleware'

const configureStore = () => {
  const middleware = [thunk]
  // middleware.push(loginMiddleware)
  // middleware.push(screenTracking)
  const reducer = combineReducers(reducers)
  const initialState = Immutable.Map()

  const store = createStore(
    reducer,
    initialState,
    composeWithDevTools(applyMiddleware(...middleware))
  )

  return store
}

export default configureStore