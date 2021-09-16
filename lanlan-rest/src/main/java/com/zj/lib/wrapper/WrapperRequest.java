package com.zj.lib.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.zj.util.CryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;


/**
 * @author xiaozj
 */
@Slf4j
public class WrapperRequest extends HttpServletRequestWrapper {
    /**
     * 存储body数据的容器
     */
    private byte[] body;

    private String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOYdkuZxSzTLNkm7EcCfcHVxL0RtdPL4Dg60NO/6of/Zhz27ulrLJh8jfQp622XRpDt80bktxMIggfdnkeW6w/OPASissq9HnN3meSfwuHMgs8uwQuag8IHagMuxnYOtzdk2wcsXwOE38dhqqyuySigeqJBMhT/YmL87nCGqKLOhg2byeuNAn3TX3nTusNZvcmXgH9BYjSW/3pKL6/9GsatQxjphx/JM+C4Ymjl3ejin3vICsapcdeft3EbLT7sOQE5ZWm1C4qaxmib3T58gd0lOT/+IH20mvUcnqYNZlJ4RLR/Ra94TBu6boRAGTsGx32NKM1c/T47BPHJYaJ/XeNAgMBAAECggEAXXV2Ektf1mpPeqn+pEHm+g32aWSDMDrE1BX13xpsAhynIyBIc5gnF6/Gkti8E69Jq4zadzgkRt3Ka+UMqDC/acnw/ZSYuJUJa67hnDeoEssYx6GxHQuuTvCPH2TDKWZOipCuDrhZA07U65wGRPX2exj2CqJ9zXstBJGUd0/0d7NANcbDQUWve8P5iSi+9TiCnj7H1DWCcTCksGxEg2ABi6bjrdD0DXs5gug8MXJMR13N9AABC4cLylKLmpYwHhCSNtJ3YfSMpm898rOWk20Y6fAzSOz9q8q4Xgny7gzPfvvxsUYMocCkp0+m/jTCMOzeQyfrl5I8HUlr7sSgyaoywQKBgQD62NlkDI6BRm6Ywv7W/GGVDWBrSXkV6TznEkxIlAaFnrG9+hF1gU1tsgSvCvx+vBPdrWdZt37tc3zxACvlJVp8VF75Jo6cpd5A4nuPh5fSyQmG7guS8+RUivv22j7nCbP1ZeNytlDdWYlwzpWTvQgHnRqxfEcOJFRrZasEzpyMhQKBgQDSnysJEdyYZRIfLzsU9sBGEvmRgAF/OO+zEA9dG5ZaSEVaIzjkE5olkkILLhaZ74lZoR/9/MLmZYKJtsxsxmh7iM5k+yPRkLsc4LfMD3wjHbUbOgieIMojAvyJKLzNlFF52NujcddhoI+xDDbRENuCRnwrc86oWTGr7to9tJcRaQKBgD6aiAa0K4yP62IsqDU3X5M3d2zPNW0GfLCenHMwnkASzwE+u8S7tHtABnM5JrLqdXrJoBV/+imAvRnYlRvnKqIE3H8J5GWr77/5xiSYDogIrFwEZNcUD1QaK6/7398BSOxhtVYXsi7L/cUtiZ8JekuMJfFNtt4MqxTylq/ocKGRAoGBALZPwFihdWkUU5NatrF2xQfi9NPVEXamOhWtGR9m+cY8OIvFYUFLFmrfJvu2cSneBe3nYmHfoHT9+PhrmpetRlNoH/+Yw3Bq7wwYGAAyhyl+VX2zxjHaAB4+P4oaEROCBuSJqdTYfa6r5LVD9U8SYG9rw595JrDiQ/SnU5GF+B3BAoGBAOl9bJpynK2N6XAnEUXdnM/QCCBlMFjWC18184Ulyehz8rjfRcTm2hsaKQ4agqMBxUsfwvJQFkd7lltKTD9m+owQG6ARmj6U6FF9x2IYSHvt5V4Xdm2pDuo/Rnlna59px6Ucsr4d+EO5Oqu/XO4B+BAgVKit28ZLDYhjJxCfn94F";

    public WrapperRequest(HttpServletRequest request) throws IOException {
        super(request);
        //接下来的request使用这个
        body = getBodyString(request);

    }

    /**
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public byte[] getBodyString(HttpServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    private byte[] inputStream2String(InputStream inputStream) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        byte[] decode = Base64.getDecoder().decode(sb.toString());
        byte[] decrypt = CryptUtil.decrypt(decode, privateKeyStr);
        return decrypt;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}


