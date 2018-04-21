/**
 * 基础样式，例如字体大小，颜色，宽高等。
 */
'use strict'

/**
 * @import
 */

import {
  StyleSheet,
  Dimensions,
  PixelRatio,
  Platform
} from 'react-native'

/**
 *屏幕的宽高，单位应该是dp
 *iPhone6 下 width为375，height为667
 */
var {
  width,
  height
} = Dimensions.get('window')

var scale = (width / 375).toFixed(6)
var heightScale = height / 677

export const rem = 1 * scale

export default {
  height: Platform.OS === 'android' ? height - 22 : height,
  width: width,
  pixelRatio: PixelRatio.get(),
  borderWidth: StyleSheet.hairlineWidth,
  rem: 1 * scale,
  scale: scale,
  heightScale: heightScale,
  percent: (num) => {
    return (num * 100) + '%'
  },
  numberFontFamily: 'DINPro-Bold',
  colors: {
    theme: '#6884FF',
    borderColor: '#ECECEC',
    buttonDisable: '#CECECE',
    backgroundColor: 'rgb(243,245,244)',
  },
  shadow: {
    shadowColor: 'rgba(41,205,251,0.10)',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: .8,
    shadowRadius: 2,
    elevation: 1,
  }
}