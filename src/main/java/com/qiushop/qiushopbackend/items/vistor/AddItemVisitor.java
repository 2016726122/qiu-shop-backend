package com.qiushop.qiushopbackend.items.vistor;

import com.qiushop.qiushopbackend.items.composite.AbstractProductItem;
import com.qiushop.qiushopbackend.items.composite.ProductComposite;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddItemVisitor implements ItemVisitor<AbstractProductItem>{

    //注入 redisProcessor
    @Autowired
    private RedisCommonProcessor redisProcessor;

    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {
        //从 Redis 中获取当前缓存数据（商品类目树形结构数据）
        ProductComposite currentItem = (ProductComposite) redisProcessor.get("items");
        //需要新增的商品类目
        ProductComposite addItem = (ProductComposite) productItem;
        //如果新增节点的父节点为当前节点，则直接添加
        if (addItem.getPid() == currentItem.getId()) {
            currentItem.addProductItem(addItem);
            return currentItem;
        }
        //通过 addChild 方法进行递归寻找新增类目的插入点
        addChild(addItem, currentItem);
        return currentItem;
    }

    //递归寻找新增类目的插入点
    private void addChild(ProductComposite addItem, ProductComposite currentItem) {
        for (AbstractProductItem abstractItem : currentItem.getChild()) {
            ProductComposite item = (ProductComposite) abstractItem;
            if (item.getId() == addItem.getPid()) {
                item.addProductItem(addItem);
                break;
            } else {
                addChild(addItem,item);
            }
        }
    }
}
