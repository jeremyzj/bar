import React, { Component } from 'react'
import { View, Text, TextInput, StyleSheet, TouchableOpacity, ActivityIndicator } from 'react-native'
import Base from '../styles/base'
const { rem } = Base
import { TextInputMask } from 'react-native-masked-text'
import * as LoginModel from '../modules/login'
const { getCodeStatus } = LoginModel
import { connect } from 'react-redux'
import Toast from 'react-native-root-toast'

class UserLogin extends Component {
  state = {
    code: '',
    mobile: '',
    codeStatus: getCodeStatus.GETCODE_STATUS_IDEL,
    countDown: 60,
    logging: false
  }

  componentWillUnmount() {
    this.interval && clearInterval(this.interval)
  }

  _onChangeMobileText(text) {
    this.setState({
      mobile: text
    })
  }

  _onChangeCodeText(text) {
    this.setState({
      code: text
    })
  }

  _getCodeHandler() {
    const { mobile } = this.state
    const { getCode } = this.props
    this.setState({ codeStatus: getCodeStatus.GETCODE_STATUS_ING })
    getCode({ phone: mobile.replace(/\s+/g, '') }).then(() => {
      this.setState({ codeStatus: getCodeStatus.GETCODE_STATUS_DONE })
      this._startCountDonw()
      this.codeTextInput && this.codeTextInput.focus()
    }).catch(() => {
      Toast.show('获取验证码失败')
      this.setState({ codeStatus: getCodeStatus.GETCODE_STATUS_FAIL })
    })
  }

  _startCountDonw() {
    this.interval = setInterval(() => {
      let countDownNumber = this.state.countDown
      if (countDownNumber === 0) {
        this.interval && clearInterval(this.interval)
        this.setState({
          countDown: 60,
          codeStatus: getCodeStatus.GETCODE_STATUS_IDEL
        })
      } else {
        this.setState({
          countDown: countDownNumber - 1
        })
      }
    }, 1000)
  }

  _login() {
    const { navigation } = this.props

    this.setState({
      logging: true
    })
    const { code, mobile } = this.state

    let phone = mobile.replace(/\s+/g, '')

    if (!mobile) {
      Toast.show('请输入手机号')
      return
    }

    if (!code) {
      Toast.show('请输入验证码')
      return
    }

    this.props.login({ phone: phone, code: code }).then(() => {
      this.setState({ logging: false })
      return navigation.goBack()
    }).catch((error) => {
      console.log('login error', error)
      Toast.show('登录失败')
      this.setState({ logging: false })
    })
  }

  _renderGetCodeView() {
    const { codeStatus } = this.state
    if (codeStatus === getCodeStatus.GETCODE_STATUS_IDEL ||
      codeStatus === getCodeStatus.GETCODE_STATUS_FAIL) {
      return (
        <TouchableOpacity onPress={this._getCodeHandler.bind(this)}>
          <View style={styles.getCode}>
            <Text style={styles.getCodeText}>获取验证码</Text>
          </View>
        </TouchableOpacity>
      )
    } else if (codeStatus === getCodeStatus.GETCODE_STATUS_ING) {
      return (
        <View style={styles.getCode}>
          <ActivityIndicator
            size='small'
            color="white"
          />
        </View>
      )
    } else {
      return (
        <View style={styles.getCodeDone}>
          <Text style={styles.countDown}>{this.state.countDown}s后重试</Text>
        </View>
      )
    }
  }

  _renderLoginButton() {
    const { logging } = this.state
    if (logging) {
      return (
        <View style={styles.login}>
          <ActivityIndicator
            size='small'
            color="white"
          />
        </View>
      )
    } else {
      return (
        <TouchableOpacity onPress={() => { this._login() }}>
          <View style={styles.login}>
            <Text style={styles.loginText}>登录</Text>
          </View>
        </TouchableOpacity>
      )
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.inputMobile}>输入手机号</Text>
        <View style={styles.from}>
          <View style={styles.mobileRow}>
            <TextInputMask
              type={'custom'}
              options={{ mask: '999 9999 9999' }}
              style={styles.mobileInput}
              onChangeText={this._onChangeMobileText.bind(this)}
              placeholder="请输入您的手机号"
              placeholderTextColor='#999'
              value={this.state.mobile}
              keyboardType="phone-pad"
              autoFocus={true}
              underlineColorAndroid="transparent"
            />
            {this._renderGetCodeView()}
          </View>
          <TextInput
            ref={(ref) => { this.codeTextInput = ref }}
            style={styles.codeInput}
            onChangeText={this._onChangeCodeText.bind(this)}
            value={this.state.code}
            placeholder='验证码'
            keyboardType="phone-pad"
            placeholderTextColor='#999'
            underlineColorAndroid="transparent"
          />
        </View>
        <View style={{ height: 78 * rem }} />
        {this._renderLoginButton()}
      </View>
    )
  }
}


const mapStateToProps = () => ({

})

const mapDispatchToProps = (dispatch) => ({
  login: query => dispatch(LoginModel.actions[LoginModel.types.LOGIN](query)),
  getCode: query => dispatch(LoginModel.actions[LoginModel.types.CODE](query))
})

export default connect(mapStateToProps, mapDispatchToProps)(UserLogin)

let styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff'
  },
  inputMobile: {
    marginTop: 86 * rem,
    marginLeft: 36 * rem,
    color: '#414852',
    fontSize: 26 * rem,
    fontWeight: '600'
  },
  from: {
    marginLeft: 36 * rem,
    marginRight: 36 * rem,
    marginTop: 38 * rem
  },
  mobileRow: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  mobileInput: {
    flex: 1,
    height: 60 * rem,
    borderBottomColor: '#ECECEC',
    borderBottomWidth: Base.borderWidth,
    fontSize: 14 * rem
  },
  codeInput: {
    height: 60 * rem,
    borderBottomColor: '#ECECEC',
    borderBottomWidth: Base.borderWidth,
    fontSize: 14 * rem
  },
  getCode: {
    width: 94 * rem,
    height: 36 * rem,
    borderRadius: 18 * rem,
    backgroundColor: Base.colors.theme,
    alignItems: 'center',
    justifyContent: 'center'
  },
  getCodeDone: {
    width: 94 * rem,
    height: 36 * rem,
    borderRadius: 18 * rem,
    backgroundColor: '#CECECE',
    alignItems: 'center',
    justifyContent: 'center'
  },
  getCodeText: {
    color: '#fff',
    fontSize: 13 * rem
  },
  login: {
    marginLeft: 24 * rem,
    marginRight: 24 * rem,
    height: 44 * rem,
    borderRadius: 22 * rem,
    backgroundColor: Base.colors.theme,
    alignItems: 'center',
    justifyContent: 'center'
  },
  loginText: {
    color: '#fff',
    fontSize: 15 * rem,
    fontWeight: '600'
  },
  countDown: {
    color: '#FFF',
    fontSize: 13 * rem
  }
})