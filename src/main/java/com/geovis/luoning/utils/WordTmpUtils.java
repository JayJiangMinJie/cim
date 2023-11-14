package com.geovis.luoning.utils;

//import com.aspose.words.Document;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.render.compute.EnvModel;
import com.deepoove.poi.render.compute.RenderDataCompute;
import com.deepoove.poi.render.processor.DocumentProcessor;
import com.deepoove.poi.resolver.TemplateResolver;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.ReflectionUtils;
import com.deepoove.poi.util.TableTools;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Word 模板工具类
 * -- 配置属性是非多线程安全
 * -- 注意使用方法前，需确保已经初始化完成（init 方法）
 *
 * @author linrf
 * @version V1.0
 * @date 2020/12/18 10:24
 */
public enum WordTmpUtils {
    /**
     * 工具实例
     */
    use;

    /**
     * ThreadLocal
     */
    private static final InheritableThreadLocal<Map.Entry<String, InputStream>> isThreadLocal = new InheritableThreadLocal<>();
    /**
     * 默认缓存 大小
     */
    private final int defBufferSize = 8 * 128 * 1024;
    /**
     * 默认编码
     */
    private final Charset defEncoding = StandardCharsets.UTF_8;
    /**
     * pdf 生成线程池
     */
    private final ThreadPoolExecutor pdfThreadPool;
    /**
     * poi-tl 配置类
     */
    private final Configure configure;
    /**
     * freemarker 配置类
     */
    private final Configuration configuration;
    /**
     * apose.words默认的License
     */
    private final byte[] defAposeWordsLicense;
    /**
     * 加载apose.words的License输入流
     */
    private final Function<String, InputStream> loadAposeWordsLicense;
    /**
     * 缓存 大小
     */
    private int bufferSize = defBufferSize;
    /**
     * apose.words的License路径--不指定使用默认License
     */
    private String aposeWordsLicensePath;
    /**
     * 使用apose.words转换pdf，默认使用
     * --否则通过itext转换（格式可能不完整）
     */
    private boolean pdfByAposeWords = true;

    /**
     * 初始化
     */
    WordTmpUtils() {
        // poi-tl
        configure = Configure.builder()
                .addPlugin('*', new ExHackLoopTableRenderPolicy())
                .useSpringEL().build();
        // freemarker
        configuration = new Configuration(Configuration.VERSION_2_3_0);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateLoader(new ExTemplateLoader());
        // pdf 生成线程池
        // 核心线程数
        int corePoolSize = Math.max(Runtime.getRuntime().availableProcessors() >> 1, 1);
        pdfThreadPool = new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize << 1,
                1, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(defBufferSize),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });
        // apose.words 默认License
        defAposeWordsLicense = ("<License><Data>" +
                "<Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products>" +
                "<EditionType>Enterprise</EditionType>" +
                "<SubscriptionExpiry>20991231</SubscriptionExpiry>" +
                "<LicenseExpiry>20991231</LicenseExpiry>" +
                "<SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
                "</Data>" +
                "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
                "</License>"
        ).getBytes();
        loadAposeWordsLicense = licPath -> {
            boolean hasLic = StringUtils.hasText(licPath);
            if (!hasLic) {
                return new ByteArrayInputStream(defAposeWordsLicense);
            }
            try {
                return loadInputStream(licPath);
            } catch (IOException e) {
                throw new RuntimeException("aspose-words:License加载失败！", e);
            }
        };
    }


    // ================辅助工具================


    /**
     * 关闭流
     *
     * @param stream io流
     */
    private static <T extends Closeable> void closeIoStream(T stream) {
        if (null == stream) {
            return;
        }
        try {
            stream.close();
        } catch (IOException ignored) {
        }
    }

//    /**
//     * 获取license
//     */
//    private boolean checkAposeWordsLicense() {
//        try (InputStream inputStream = loadAposeWordsLicense.apply(aposeWordsLicensePath)) {
//            License aposeLic = new License();
//            aposeLic.setLicense(inputStream);
//            return true;
//        } catch (Exception e) {
//            throw new RuntimeException("aspose-words:License验证失败！", e);
//        }
//    }

    /**
     * 设置apose.words的License路径
     *
     * @param aposeWordsLicensePath apose.words的License路径--“classpath:***”或者“全路径”
     */
    public WordTmpUtils setAposeWordsLicensePath(String aposeWordsLicensePath) {
        this.aposeWordsLicensePath = aposeWordsLicensePath;
        return this;
    }

    /**
     * 设置是否使用apose.words生成pdf
     * --默认true,
     */
    public WordTmpUtils setPdfByAposeWords(boolean pdfByAposeWords) {
        this.pdfByAposeWords = pdfByAposeWords;
        return this;
    }

    /**
     * 设置临时缓冲区大小
     * 默认：{@link WordTmpUtils#defBufferSize}
     */
    public WordTmpUtils setBufferSize(int bufferSize) {
        this.bufferSize = Optional.of(bufferSize).filter(bs -> bs > 0).orElse(defBufferSize);
        return this;
    }

    /**
     * 加载文件输入流
     *
     * @param filePath 文件路径，“classpath:***”或者“全路径”
     */
    public InputStream loadInputStream(String filePath) throws IOException {
        if (!filePath.startsWith("classpath:")) {
            // 直接读取
            return new FileInputStream(ResourceUtils.getFile(filePath));
        }
        // 解析classpath路径
        filePath = filePath.replaceFirst("^classpath:/?", "");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(filePath);
        if (null != url && "jar".equals(url.getProtocol())) {
            // 解析jar的classpath路径
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            filePath = jarConnection.getJarEntry().getName();
        }
        return classLoader.getResourceAsStream(filePath);
    }

    /**
     * 加载文件输入流
     *
     * @param file 文件,自动创建与删除
     */
    public <T> T fileNewOrDel(File file, Function<File, T> option) {
        boolean opt = false;
        try {
            if (!file.exists()) {
                opt = file.getParentFile().mkdirs();
                opt |= file.createNewFile();
            }
            return option.apply(file);
        } catch (Throwable t) {
            if (opt) {
                opt = file.delete();
            }
            throw new RuntimeException(t);
        }

    }


    // ================辅助工具================
    // ================图片转换================


    /**
     * 图片以base64编码形式--只适用doc导出
     *
     * @param imgFilePath 图片全路径
     */
    public String img2Str(String imgFilePath) {
        try {
            return img2Str(loadInputStream(imgFilePath));
        } catch (IOException e) {
            throw new RuntimeException("图片不存在！", e);
        }
    }

    /**
     * 图片以base64编码形式--只适用doc导出
     *
     * @param imgFile 图片文件
     */
    public String img2Str(File imgFile) {
        try {
            return img2Str(new FileInputStream(imgFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("图片不存在！", e);
        }
    }

    /**
     * 图片以base64编码形式--只适用doc导出
     *
     * @param imgIn 图片输入流
     */
    public String img2Str(InputStream imgIn) {
        // 读取图片
        byte[] data = null;
        try {
            data = new byte[imgIn.available()];
            int len = imgIn.read(data);
        } catch (IOException e) {
            throw new RuntimeException("图片IO异常！", e);
        } finally {
            closeIoStream(imgIn);
        }
        // base64 编码
        return new String(Base64.getEncoder().encode(data), defEncoding);
    }

    /**
     * 图片以base64编码形式--只适用doc导出
     *
     * @param img  图片
     * @param type 图片类型，默认jpg
     */
    public String img2Str(RenderedImage img, String type) {
        type = Optional.ofNullable(type).orElse("jpg");
        // 读取图片
        byte[] data = null;
        try (ByteArrayOutputStream bOut = new ByteArrayOutputStream()) {
            ImageIO.write(img, type, bOut);
            data = bOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("图片IO异常！", e);
        }
        // base64 编码
        return new String(Base64.getEncoder().encode(data), defEncoding);
    }

    /**
     * 图片以base64编码形式--只适用poi-tl导出
     *
     * @param imgFilePath 图片全路径
     */
    public PictureRenderData img2Picture(String imgFilePath) {
        try {
            return Pictures.ofStream(loadInputStream(imgFilePath), PictureType.JPEG).create();
        } catch (IOException e) {
            throw new RuntimeException("图片不存在！", e);
        }
    }

    /**
     * 图片以base64编码形式--只适用poi-tl导出
     *
     * @param imgFile 图片文件
     */
    public PictureRenderData img2Picture(File imgFile) {
        try {
            return Pictures.ofStream(new FileInputStream(imgFile), PictureType.JPEG).create();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("图片不存在！", e);
        }
    }

    /**
     * 图片以base64编码形式--只适用poi-tl导出
     *
     * @param imgIn 图片输入流
     */
    public PictureRenderData img2Picture(InputStream imgIn) {
        // 读取图片
        return Pictures.ofStream(imgIn, PictureType.JPEG).create();
    }

    /**
     * 图片以base64编码形式--只适用poi-tl导出
     *
     * @param img  图片
     * @param type 图片类型，默认jpg
     */
    public PictureRenderData img2Picture(RenderedImage img, PictureType type) {
        type = Optional.ofNullable(type).orElse(PictureType.JPEG);
        // 读取图片
        byte[] data = null;
        try (ByteArrayOutputStream bOut = new ByteArrayOutputStream()) {
            ImageIO.write(img, type.name(), bOut);
            data = bOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("图片IO异常！", e);
        }
        return Pictures.ofBytes(data, type).create();
    }

    /**
     * 图片以base64编码形式--只适用poi-tl导出
     *
     * @param img  图片
     * @param type 图片类型，默认jpg
     */
    public PictureRenderData img2Picture(String img, PictureType type) {
        type = Optional.ofNullable(type).orElse(PictureType.JPEG);
        return Pictures.ofBase64(img, type).create();
    }


    // ================图片转换================
    // ================Word转换================


//    /**
//     * apose.words的word格式转换
//     * --支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
//     *
//     * @param inFilePath  输入文件（全路径），待转化的word文档
//     * @param outFilePath 输出文件（全路径）
//     * @param saveFormat  保存格式，见{@link SaveFormat}
//     */
//    public WordTmpUtils trans(String inFilePath, String outFilePath, int saveFormat) {
//        try {
//            return trans(loadInputStream(inFilePath), new FileOutputStream(outFilePath), saveFormat);
//        } catch (IOException e) {
//            throw new RuntimeException("文件不存在！", e);
//        }
//    }
//
//    /**
//     * apose.words的word格式转换
//     * --支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
//     *
//     * @param inFile     输入文件（全路径），待转化的word文档
//     * @param outFile    输出文件（全路径）
//     * @param saveFormat 保存格式，见{@link SaveFormat}
//     */
//    public WordTmpUtils trans(File inFile, File outFile, int saveFormat) {
//        try {
//            return trans(new FileInputStream(inFile), new FileOutputStream(outFile), saveFormat);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("文件不存在！", e);
//        }
//    }

//    /**
//     * apose.words的word格式转换
//     * --支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
//     *
//     * @param inputStream 输入流，待转化的word文档
//     * @param outStream   输出流
//     * @param saveFormat  保存格式，见{@link SaveFormat}
//     */
//    public WordTmpUtils trans(InputStream inputStream, OutputStream outStream, int saveFormat) {
//        // 验证License 若不验证则转化出的pdf文档会有水印产生
//        if (!checkAposeWordsLicense()) {
//            throw new RuntimeException("aspose-words:License验证失败！");
//        }
//        try {
//            // 待转化的word文档
//            Document doc = new Document(inputStream);
//            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
//            doc.save(outStream, saveFormat);
//        } catch (Exception e) {
//            throw new RuntimeException("文件转换失败！", e);
//        } finally {
//            closeIoStream(inputStream);
//            closeIoStream(outStream);
//        }
//        return this;
//    }


    // ================Word转换================
    // ================模板填充================


    /**
     * 填充模板，生成doc文件（.doc）
     *
     * @param tmpFilePath 模板文件路径（全路径）,word2003.xml/word2003.ftl
     * @param outFilePath 输出文件路径（全路径,自动创建/删除）*.doc/*.xml
     * @param data        填充数据
     */
    public WordTmpUtils fillToDoc(String tmpFilePath, String outFilePath, Object data) {
        return fillToDoc(tmpFilePath, new File(outFilePath), data);
    }

    /**
     * 填充模板，生成doc文件（.doc）
     *
     * @param tmpFilePath 模板文件路径（全路径）,word2003.xml/word2003.ftl
     * @param outFile     输出文件路径（全路径,自动创建/删除）*.doc/*.xml
     * @param data        填充数据
     */
    public WordTmpUtils fillToDoc(String tmpFilePath, File outFile, Object data) {
        return fileNewOrDel(outFile, f -> {
            try {
                return fillToDoc(loadInputStream(tmpFilePath), new FileOutputStream(f), data);
            } catch (IOException e) {
                throw new RuntimeException("模板文件不存在！", e);
            }
        });
    }

    /**
     * 填充模板，生成doc文件（.doc）
     * --自动关流
     * --docx(zip)内 document.ftl.ftl,不支持转doc(xml)
     *
     * @param tmpFilePath 模板文件路径（全路径）,word2003.xml/word2003.ftl
     * @param outStream   输出流（全路径）*.doc/*.xml
     * @param data        填充数据
     */
    public WordTmpUtils fillToDoc(String tmpFilePath, OutputStream outStream, Object data) {
        try {
            return fillToDoc(loadInputStream(tmpFilePath), outStream, data);
        } catch (IOException e) {
            throw new RuntimeException("文件不存在！", e);
        }
    }

    /**
     * 填充模板，生成doc文件（.doc）
     * --自动关流
     * --docx(zip)内 document.ftl.ftl,不支持转doc(xml)
     *
     * @param inputStream 模板文件输入流,word2003.xml/word2003.ftl
     * @param outStream   输出流（全路径）*.doc/*.xml
     * @param data        填充数据
     */
    public WordTmpUtils fillToDoc(InputStream inputStream, OutputStream outStream, Object data) {
        String name = UUID.randomUUID().toString() + inputStream.hashCode();
        isThreadLocal.set(new AbstractMap.SimpleEntry<>(name, inputStream));
        // 转换
        try (Writer out = new BufferedWriter(new OutputStreamWriter(outStream, defEncoding), bufferSize)) {
            Template template = configuration.getTemplate(name);
            template.process(data, out);
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件不存在！", e);
        } catch (IOException e) {
            throw new RuntimeException("文件IO异常！", e);
        } catch (TemplateException e) {
            throw new RuntimeException("文件模板错误！", e);
        } finally {
            closeIoStream(inputStream);
            closeIoStream(outStream);
            isThreadLocal.remove();
        }
        return this;
    }


    /**
     * 填充模板，生成docx文件（.docx）
     * --自动关流
     * --填充所有的xml
     *
     * @param tmpFilePath 模板文件路径（全路径）,*.docx
     * @param outFilePath 输出文件路径（全路径,自动创建/删除）*.docx
     * @param data        填充数据
     */
    public WordTmpUtils fillToDocx(String tmpFilePath, String outFilePath, Object data) {
        return fillToDocx(tmpFilePath, new File(outFilePath), data);
    }

    /**
     * 填充模板，生成docx文件（.docx）
     * --自动关流
     * --填充所有的xml
     *
     * @param tmpFilePath 模板文件路径（全路径）,*.docx
     * @param outFile     输出文件路径（全路径,自动创建/删除）*.docx
     * @param data        填充数据
     */
    public WordTmpUtils fillToDocx(String tmpFilePath, File outFile, Object data) {
        return fileNewOrDel(outFile, f -> {
            try {
                return fillToDocx(loadInputStream(tmpFilePath), new FileOutputStream(f), data);
            } catch (IOException e) {
                throw new RuntimeException("模板文件不存在！", e);
            }
        });
    }

    /**
     * 填充模板，生成docx文件（.docx）
     * --自动关流
     * --填充所有的xml
     *
     * @param tmpFilePath 模板文件路径（全路径）,*.docx
     * @param outStream   输出流
     * @param data        填充数据
     */
    public WordTmpUtils fillToDocx(String tmpFilePath, OutputStream outStream, Object data) {
        try {
            return fillToDocx(loadInputStream(tmpFilePath), outStream, data);
        } catch (IOException e) {
            throw new RuntimeException("文件不存在！", e);
        }
    }

    /**
     * 填充模板，生成docx文件（.docx）
     * --自动关流
     * --填充所有的xml
     *
     * @param inputStream 模板文件输入流,*.docx
     * @param outStream   输出流
     * @param data        填充数据
     */
    public WordTmpUtils fillToDocx(InputStream inputStream, OutputStream outStream, Object data) {
        try {
            XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(data);
            template.writeAndClose(outStream);
        } catch (IOException e) {
            throw new RuntimeException("文件IO异常！", e);
        } catch (Exception e) {
            throw new RuntimeException("模板转换异常！", e);
        } finally {
            closeIoStream(inputStream);
            closeIoStream(outStream);
        }
        return this;
    }


//    /**
//     * 填充模板，生成pdf文件（.pdf）
//     * --自动关流
//     * --填充所有的xml
//     *
//     * @param tmpFilePath 模板文件路径（全路径）,*.docx/*.zip
//     * @param outFilePath 输出文件路径（全路径,自动创建/删除）*.pdf
//     * @param data        填充数据
//     */
//    public WordTmpUtils fillToPdf(String tmpFilePath, String outFilePath, Object data) {
//        return fillToPdf(tmpFilePath, new File(outFilePath), data);
//    }

//    /**
//     * 填充模板，生成pdf文件（.pdf）
//     * --自动关流
//     * --填充所有的xml
//     *
//     * @param tmpFilePath 模板文件路径（全路径）,*.docx/*.zip
//     * @param outFile     输出文件路径（全路径,自动创建/删除）*.pdf
//     * @param data        填充数据
//     */
//    public WordTmpUtils fillToPdf(String tmpFilePath, File outFile, Object data) {
//        return fileNewOrDel(outFile, f -> {
//            try {
//                return fillToPdf(loadInputStream(tmpFilePath), new FileOutputStream(f), data);
//            } catch (IOException e) {
//                throw new RuntimeException("模板文件不存在！", e);
//            }
//        });
//    }
//
//    /**
//     * 填充模板，生成pdf文件（.pdf）
//     * --自动关流
//     * --填充所有的xml
//     *
//     * @param tmpFilePath 模板文件路径（全路径）,*.docx/*.zip
//     * @param outStream   输出流（全路径）*.pdf
//     * @param data        填充数据
//     */
//    public WordTmpUtils fillToPdf(String tmpFilePath, OutputStream outStream, Object data) {
//        try {
//            return fillToPdf(loadInputStream(tmpFilePath), outStream, data);
//        } catch (IOException e) {
//            throw new RuntimeException("文件不存在！", e);
//        }
//    }

//    /**
//     * 填充模板，生成pdf文件（.pdf）
//     * --自动关流
//     * --填充所有的xml
//     *
//     * @param inputStream 模板输入流,*.docx/*.zip
//     * @param outStream   输出流*.pdf
//     * @param data        填充数据
//     */
//    public WordTmpUtils fillToPdf(InputStream inputStream, OutputStream outStream, Object data) {
//        // pipe 通道 转换
//        final AtomicReference<Throwable> tx = new AtomicReference<>();
//        try (PipedOutputStream pOut = new PipedOutputStream();
//             PipedInputStream pIn = new PipedInputStream(pOut, bufferSize);) {
//            // 转换docx 新线程执行
//            pdfThreadPool.submit(() -> {
//                try {
//                    fillToDocx(inputStream, pOut, data);
//                } catch (Throwable t) {
//                    // 设置错误
//                    tx.set(t);
//                }
//            });
//            // 导出pdf
//            if (pdfByAposeWords) {
//                // apose.words方式
//                trans(pIn, outStream, SaveFormat.PDF);
//                return this;
//            }
//            // itext 方式
//            XWPFDocument document = new XWPFDocument(pIn);
//            PdfConverter.getInstance().convert(document, outStream, PdfOptions.create());
//        } catch (IOException e) {
//            Optional.ofNullable(tx.get()).ifPresent(e::addSuppressed);
//            throw new RuntimeException("文件IO异常！", e);
//        } finally {
//            closeIoStream(inputStream);
//            closeIoStream(outStream);
//        }
//        return this;
//    }


    // ================模板填充================


    /**
     * 自定义poi-tl 模板工具:表格行循环
     * --拓展{@link com.deepoove.poi.policy.HackLoopTableRenderPolicy}
     * --参考，区块集合迭代器{@link com.deepoove.poi.render.processor.AbstractIterableProcessor}
     *
     * <p>
     * 表格行环境信息变量：
     * _pos		int	返回当前迭代从1开始的索引
     * _is_first	boolean	辨别循环项是否是当前迭代的第一项。
     * _is_last	boolean	辨别循环项是否是当前迭代的最后一项。
     * _has_next	boolean	辨别循环项是否是有下一项。
     * _is_even_item	boolean	辨别循环项是否是当前迭代间隔1的奇数项。
     * _is_odd_item	boolean	辨别循环项是否是当前迭代间隔1的偶数项。
     * _this    object  引用当前对象。
     * #this    object  引用当前对象，由于#和已有表格标签标识冲突，所以在文本标签中需要使用=号标识来输出文本。仅单独使用，不与上述参数同用
     * <p>
     * #this 识别具体见{@link com.deepoove.poi.render.compute.DefaultELRenderDataCompute}
     */
    public static class ExHackLoopTableRenderPolicy implements RenderPolicy {
        /**
         * 表格参数前缀，默认：[
         */
        private final String prefix;
        /**
         * 表格参数后缀，默认：]
         */
        private final String suffix;
        /**
         * 表格数据是否同行,默认false
         */
        private final boolean onSameLine;

        public ExHackLoopTableRenderPolicy() {
            this(false);
        }

        public ExHackLoopTableRenderPolicy(boolean onSameLine) {
            this("[", "]", onSameLine);
        }

        public ExHackLoopTableRenderPolicy(String prefix, String suffix) {
            this(prefix, suffix, false);
        }

        public ExHackLoopTableRenderPolicy(String prefix, String suffix, boolean onSameLine) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.onSameLine = onSameLine;
        }

        @Override
        public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
            RunTemplate runTemplate = (RunTemplate) eleTemplate;
            XWPFRun run = runTemplate.getRun();
            try {
                if (!TableTools.isInsideTable(run)) {
                    throw new IllegalStateException(
                            "The template tag " + runTemplate.getSource() + " must be inside a table");
                }
                XWPFTableCell tagCell = (XWPFTableCell) ((XWPFParagraph) run.getParent()).getBody();
                XWPFTable table = tagCell.getTableRow().getTable();
                run.setText("", 0);

                int templateRowIndex = getTemplateRowIndex(tagCell);
                if (data instanceof Iterable) {
                    Iterator<?> iterator = ((Iterable<?>) data).iterator();
                    XWPFTableRow templateRow = table.getRow(templateRowIndex);
                    int insertPosition = templateRowIndex;

                    TemplateResolver resolver = new TemplateResolver(template.getConfig().copy(prefix, suffix));
                    boolean firstFlag = true;
                    // 表格行环境信息变量
                    boolean hasNext = false;
                    Map<String, Object> env = new HashMap<>();
                    while (iterator.hasNext()) {
                        insertPosition = templateRowIndex++;
                        XWPFTableRow nextRow = table.insertNewTableRow(insertPosition);
                        setTableRow(table, templateRow, insertPosition);

                        // double set row
                        XmlCursor newCursor = templateRow.getCtRow().newCursor();
                        newCursor.toPrevSibling();
                        XmlObject object = newCursor.getObject();
                        nextRow = new XWPFTableRow((CTRow) object, table);
                        if (!firstFlag) {
                            // update VMerge cells for non-first row
                            List<XWPFTableCell> tableCells = nextRow.getTableCells();
                            for (XWPFTableCell cell : tableCells) {
                                CTTcPr tcPr = TableTools.getTcPr(cell);
                                CTVMerge vMerge = tcPr.getVMerge();
                                if (null == vMerge) {
                                    continue;
                                }
                                if (STMerge.RESTART == vMerge.getVal()) {
                                    vMerge.setVal(STMerge.CONTINUE);
                                }
                            }
                        } else {
                            firstFlag = false;
                        }
                        setTableRow(table, nextRow, insertPosition);
                        // 添加表格行环境信息
                        Object root = iterator.next();
                        hasNext = iterator.hasNext();
                        env.put("_pos", insertPosition);
                        env.put("_is_first", insertPosition == 0);
                        env.put("_is_last", !hasNext);
                        env.put("_has_next", hasNext);
                        env.put("_is_even_item", insertPosition % 2 == 1);
                        env.put("_is_odd_item", insertPosition % 2 == 0);
                        env.put("_this", root);
                        // 忽略#this,仅单独使用，不与上述参数同用
                        RenderDataCompute dataCompute = template.getConfig().getRenderDataComputeFactory()
                                .newCompute(EnvModel.of(root, env));
                        List<XWPFTableCell> cells = nextRow.getTableCells();
                        cells.forEach(cell -> {
                            List<MetaTemplate> templates = resolver.resolveBodyElements(cell.getBodyElements());
                            new DocumentProcessor(template, resolver, dataCompute).process(templates);
                        });
                    }
                }

                table.removeRow(templateRowIndex);
                afterloop(table, data);
            } catch (Exception e) {
                throw new RenderException("HackLoopTable for " + eleTemplate + "error: " + e.getMessage(), e);
            }
        }

        private int getTemplateRowIndex(XWPFTableCell tagCell) {
            XWPFTableRow tagRow = tagCell.getTableRow();
            return onSameLine ? getRowIndex(tagRow) : (getRowIndex(tagRow) + 1);
        }

        protected void afterloop(XWPFTable table, Object data) {
        }

        private void setTableRow(XWPFTable table, XWPFTableRow templateRow, int pos) {
            @SuppressWarnings("unchecked")
            List<XWPFTableRow> rows = (List<XWPFTableRow>) ReflectionUtils.getValue("tableRows", table);
            rows.set(pos, templateRow);
            table.getCTTbl().setTrArray(pos, templateRow.getCtRow());
        }

        private int getRowIndex(XWPFTableRow row) {
            List<XWPFTableRow> rows = row.getTable().getRows();
            return rows.indexOf(row);
        }
    }

    /**
     * 自定义模板加载类
     */
    public static class ExTemplateLoader implements TemplateLoader {
        @Override
        public Object findTemplateSource(String name) throws IOException {
            return Optional.ofNullable(WordTmpUtils.isThreadLocal.get())
                    .filter(kv -> name.equals(kv.getKey()))
                    .map(Map.Entry::getValue)
                    .orElse(null);
        }

        @Override
        public long getLastModified(Object templateSource) {
            if (null == WordTmpUtils.isThreadLocal.get()) {
                return 0;
            }
            return 1;
        }

        @Override
        public Reader getReader(Object templateSource, String encoding) throws IOException {
            if (!(templateSource instanceof InputStream)) {
                return null;
            }
            return new InputStreamReader((InputStream) templateSource, encoding);
        }

        @Override
        public void closeTemplateSource(Object templateSource) throws IOException {
            Optional.ofNullable(WordTmpUtils.isThreadLocal.get())
                    .map(Map.Entry::getValue)
                    .ifPresent(WordTmpUtils::closeIoStream);
        }
    }
}
