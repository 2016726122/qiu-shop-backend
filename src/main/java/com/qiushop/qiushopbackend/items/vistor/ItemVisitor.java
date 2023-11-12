package com.qiushop.qiushopbackend.items.vistor;

import com.qiushop.qiushopbackend.items.composite.AbstractProductItem;

//此处使用泛型 T 进行接口定义，提高代码的扩展性
public interface ItemVisitor<T> {
    //定义公共的 visitor 方法供子类实现
    T visitor(AbstractProductItem productItem);
}
