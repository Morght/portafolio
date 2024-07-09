package com.morght.literalura.utils;

public interface IJsonTransform {

    <T> T toClass(String json, Class<T> clase);
}