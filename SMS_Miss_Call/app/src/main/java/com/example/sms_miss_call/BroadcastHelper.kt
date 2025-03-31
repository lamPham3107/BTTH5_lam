package com.example.sms_miss_call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.util.Log

class BroadcastHelper : BroadcastReceiver() {
    companion object {
        private var wasRinging = false
        private var lastIncomingNumber: String? = null
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Khi có cuộc gọi đến, lưu số điện thoại
                    wasRinging = true
                    lastIncomingNumber = incomingNumber
                    Log.d("CallReceiver", "Incoming call from: $lastIncomingNumber")
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    if (wasRinging && lastIncomingNumber != null) {
                        sendMissedCallSMS(context, lastIncomingNumber!!)
                    }
                    wasRinging = false
                    lastIncomingNumber = null
                }
            }
        }
    }
    private fun sendMissedCallSMS(context: Context?, phoneNumber: String) {
        val message = "Xin chào! Tôi đã bỏ lỡ cuộc gọi của bạn. Vui lòng nhắn tin lại."

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallReceiver", "Sent SMS to: $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallReceiver", "Failed to send SMS", e)
        }
    }
}