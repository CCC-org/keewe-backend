package ccc.keeweapi.utils.annotations;


import ccc.keewecore.consts.TitleCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleEventPublish {
    TitleCategory titleCategory();
}
