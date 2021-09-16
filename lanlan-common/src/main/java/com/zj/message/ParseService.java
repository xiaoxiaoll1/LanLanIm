package com.zj.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.zj.exception.BusinessException;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;

import java.util.HashMap;
import java.util.Map;

public class ParseService {

    private Map<MsgTypeEnum, Parse> parseFunctionMap;

    public ParseService() {
        parseFunctionMap = new HashMap<>(MsgTypeEnum.values().length);

        parseFunctionMap.put(MsgTypeEnum.CHAT, Chat.ChatMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.INTERNAL, Internal.InternalMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.ACK, Ack.AckMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.CHECK, Check.CheckMsg::parseFrom);
    }

    public Message getMsgByCode(int code, byte[] bytes) throws InvalidProtocolBufferException {
        MsgTypeEnum msgType = MsgTypeEnum.getByCode(code);
        Parse parseFunction = parseFunctionMap.get(msgType);
        if (parseFunction == null) {
            throw new BusinessException("[msg parse], no proper parse function, msgType: " + msgType.name());
        }
        return parseFunction.process(bytes);
    }

    @FunctionalInterface
    public interface Parse {
        /**
         * parse msg
         *
         * @param bytes msg bytes
         * @return
         * @throws InvalidProtocolBufferException
         */
        Message process(byte[] bytes) throws InvalidProtocolBufferException;
    }
}