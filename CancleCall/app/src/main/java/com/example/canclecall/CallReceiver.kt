package com.example.canclecall

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat

class CallReceiver : BroadcastReceiver() {

    // Danh sách số điện thoại bị chặn
    private val blockedNumbers = setOf("0984546458", "0384719169")

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.d("CallReceiver", "Trạng thái cuộc gọi: $state, Số gọi đến: $incomingNumber")

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber in blockedNumbers) {
                Log.d("CallReceiver", "Chặn cuộc gọi từ: $incomingNumber")
                rejectCall(context)
            }
        }
    }

    private fun rejectCall(context: Context?) {
        if (context == null) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as android.telecom.TelecomManager

                // Kiểm tra quyền trước khi gọi endCall()
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                    telecomManager.endCall()
                    Log.d("CallReceiver", "Đã từ chối cuộc gọi thành công")
                } else {
                    Log.e("CallReceiver", "Không có quyền ANSWER_PHONE_CALLS")
                }
            } else {
                Log.e("CallReceiver", "Không thể từ chối cuộc gọi trên Android < 9")
            }
        } catch (e: SecurityException) {
            Log.e("CallReceiver", "Lỗi bảo mật khi từ chối cuộc gọi", e)
        } catch (e: Exception) {
            Log.e("CallReceiver", "Lỗi khi từ chối cuộc gọi", e)
        }
    }
}
