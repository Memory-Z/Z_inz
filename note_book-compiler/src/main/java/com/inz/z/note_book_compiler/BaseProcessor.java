package com.inz.z.note_book_compiler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 14:15.
 */
public abstract class BaseProcessor extends AbstractProcessor {

    Filer mFiler;
    Types types;
    Elements elements;
    Logger logger;
    String moduleName = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());


    }
}
