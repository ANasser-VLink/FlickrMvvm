package com.subwilven.basemodel.project_base.POJO


import com.subwilven.basemodel.R

class ErrorModel(val title: Message, val message: Message) {

    companion object {

        fun noConnection(): ErrorModel {
            return ErrorModel(Message(R.string.ibase_no_internet_connection), Message(R.string.ibase_check_your_mobile_data_or_wi_fi))
        }

        fun timeOut(): ErrorModel {
            return ErrorModel(Message(R.string.ibase_server_cannot_be_reached), Message(R.string.ibase_please_try_again_later))
        }
        fun serverError(msg:Message): ErrorModel {
            return ErrorModel(msg, Message(R.string.ibase_please_try_again_later))
        }

    }

}
