package com.kevinvg.umalauncherj.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MsgPackHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MsgPackHandler() {}

    // Boilerplate nonsense
    private static Object unpack(MessageUnpacker unpacker) throws IOException {
        if (!unpacker.hasNext()) {
            return null;
        }

        switch (unpacker.getNextFormat().getValueType()) {
            case INTEGER:
                return unpacker.unpackLong();
            case FLOAT:
                return unpacker.unpackDouble();
            case STRING:
                return unpacker.unpackString();
            case BOOLEAN:
                return unpacker.unpackBoolean();
            case BINARY:
                return unpacker.readPayload(unpacker.unpackBinaryHeader());
            case ARRAY:
                int arraySize = unpacker.unpackArrayHeader();
                List<Object> list = new ArrayList<>(arraySize);
                for (int i = 0; i < arraySize; i++) {
                    list.add(unpack(unpacker));
                }
                return list;
            case MAP:
                int mapSize = unpacker.unpackMapHeader();
                Map<Object, Object> map = HashMap.newHashMap(mapSize);
                for (int i = 0; i < mapSize; i++) {
                    Object key = unpack(unpacker);
                    Object value = unpack(unpacker);
                    map.put(key, value);
                }
                return map;
            case NIL:
                unpacker.unpackNil();
                return null;
            default:
                throw new IOException("Unsupported MessagePack type");
        }
    }

    public static Object readMsgPack(byte[] msgPackData) throws IOException {
        try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(msgPackData)) {
            return unpack(unpacker);
        }
    }

    public static JsonNode responseMsgpackToJsonNode(Path responsePath) {
        try {
            var bytes = Files.readAllBytes(responsePath);
            return objectMapper.valueToTree(readMsgPack(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode requestMsgPackToJsonNode(Path requestPath) {
        try {
            var bytes = Files.readAllBytes(requestPath);
            bytes = Arrays.copyOfRange(bytes, 170, bytes.length);
            return objectMapper.valueToTree(readMsgPack(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
