package com.example.commonlibrary.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by COOTEK on 2017/8/13.
 */


@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}
