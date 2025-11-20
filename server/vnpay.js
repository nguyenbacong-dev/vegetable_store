import express from 'express'
import moment from 'moment'
import crypto from 'crypto'
import qs from 'qs'
import cors from 'cors'

const app = express()
app.use(express.json())
app.use(cors())

// 🔹 Thông tin từ email của bạn
const vnp_TmnCode = 'BZB7GFR9'
const vnp_HashSecret = 'CO8SQPC2LSZIUOZJB2UDN34IELA8PQI2'
const vnp_Url = 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html'

// 🔹 URL trả về sau khi thanh toán (frontend đang chạy localhost:5173)
const vnp_ReturnUrl = 'http://localhost:5173/checkout'

function sortObject(obj) {
  const sorted = {}
  const keys = Object.keys(obj).sort()
  for (let key of keys) sorted[key] = obj[key]
  return sorted
}

// ✅ API tạo link thanh toán VNPAY
app.post('/api/create_payment_url', (req, res) => {
  const { amount, orderId } = req.body
  if (!amount || !orderId)
    return res.status(400).json({ message: 'Thiếu amount hoặc orderId' })

  const ipAddr =
    req.headers['x-forwarded-for'] || req.connection.remoteAddress || req.socket.remoteAddress

  let vnp_Params = {}
  vnp_Params['vnp_Version'] = '2.1.0'
  vnp_Params['vnp_Command'] = 'pay'
  vnp_Params['vnp_TmnCode'] = vnp_TmnCode
  vnp_Params['vnp_Locale'] = 'vn'
  vnp_Params['vnp_CurrCode'] = 'VND'
  vnp_Params['vnp_TxnRef'] = orderId
  vnp_Params['vnp_OrderInfo'] = `Thanh toán đơn hàng #${orderId}`
  vnp_Params['vnp_OrderType'] = 'other'
  vnp_Params['vnp_Amount'] = amount * 100
  vnp_Params['vnp_ReturnUrl'] = vnp_ReturnUrl
  vnp_Params['vnp_IpAddr'] = ipAddr
  vnp_Params['vnp_CreateDate'] = moment().format('YYYYMMDDHHmmss')

  vnp_Params = sortObject(vnp_Params)
  const signData = qs.stringify(vnp_Params, { encode: false })
  const hmac = crypto.createHmac('sha512', vnp_HashSecret)
  const signed = hmac.update(Buffer.from(signData, 'utf-8')).digest('hex')
  vnp_Params['vnp_SecureHash'] = signed

  const paymentUrl = vnp_Url + '?' + qs.stringify(vnp_Params, { encode: false })
  res.json({ paymentUrl })
})

// 🔹 Chạy server backend
app.listen(3000, () => console.log('✅ VNPAY server đang chạy tại http://localhost:3000'))
