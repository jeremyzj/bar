/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react'
import {View} from  'react-native'
import configureStore from './store'
import Home from './pages/home'
import Login from './pages/login'
import { Provider } from 'react-redux'

export default class App extends Component {
  constructor() {
    super()
  }

  renderPages() {
    const {page} = this.props
    switch (page) {
      case 'home':
        return <Home/>
      case 'login':
        return <Login/>
      default:
        return <View/>
    }
  }

  render() {
    return <Provider store={configureStore()}>
      {this.renderPages()}
    </Provider>
  }
}

