package msg;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import com.googlecode.protobuf.format.JsonFormat;
import com.zj.protobuf.Chat;
import com.zj.util.IdWorker;
import io.netty.util.CharsetUtil;
import org.junit.Test;

public class TestProto {

    @Test
    public void test1() throws JsonFormat.ParseException {
        Chat.ChatMsg chat = Chat.ChatMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFromUser("zj")
                .setDestUser("lanlan")
                .setFromModule(Chat.ChatMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .setMsgBody("nihao")
                .build();

        String text = JsonFormat.printToString(chat);
        System.out.println(text);
        Chat.ChatMsg.Builder builder = Chat.ChatMsg.newBuilder();
        JsonFormat.merge(text, builder);
        Chat.ChatMsg build = builder.build();
        System.out.println(build.getId());
    }
}
