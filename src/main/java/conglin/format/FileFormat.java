package conglin.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;
import java.io.*;
/**
 * 使用json文件 声明 明文/密文 和 密钥
 */
@Data
public class FileFormat{
    private long text;
    private long[] keys;

    @SneakyThrows
    public static FileFormat readFile(String filename){
        //解析json文件
        @Cleanup Reader reader = new InputStreamReader(new FileInputStream(filename + ".json"), "UTF-8");
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, FileFormat.class);
    }

    @SneakyThrows
    public static void writeFile(FileFormat fileFormat, String filename){
        @Cleanup Writer writer = new OutputStreamWriter(new FileOutputStream(filename + ".json"), "UTF-8");
        Gson gson = new GsonBuilder().create();
        writer.write(gson.toJson(fileFormat));
    }
}